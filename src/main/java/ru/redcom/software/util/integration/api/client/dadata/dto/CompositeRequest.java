/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

/*
Запрос типа "Составная запись".

{
  "structure": [
    "AS_IS",
    "NAME",
    "ADDRESS",
    "PHONE" ],
  "data": [
    [ "1",
      "Федотов Алексей",
      "Москва, Сухонская улица, 11 кв 89",
      "8 916 823 3454"
    ],
    [ ["2"],
      ["Иванов", "Сергей Владимирович"],
      ["мск", "улица свободы", "65", "12"],
      ["495 663-12-53"]
    ],
    [ "3",
      ["Ольга Павловна", "Ященко"],
      ["", "Спб, ул Петрозаводская 8", "", ""],
      "457 07 25"
    ]
  ]
}
*/
@ToString
public class CompositeRequest {

	public enum ElementType {
		AS_IS, NAME, ADDRESS, BIRTHDATE, PASSPORT, PHONE, EMAIL, VEHICLE
	}

	// These collections are iterated in consistent ascending order of their key type ordinal
	// Structure of request (element types)
	@JsonProperty("structure")
	@Nonnull
	private final EnumSet<ElementType> elementTypes;
	// forms data
	@JsonProperty("data")
	@Nonnull
	private List<Element> contents = new LinkedList<>();


	// Instance sholud be only constructed by builder
	private CompositeRequest(@Nonnull final EnumSet<ElementType> elementTypes) {
		this.elementTypes = elementTypes;
	}

	private void stripEmptyElements() {
		contents = contents.stream().filter(Element::nonEmpty).collect(Collectors.toList());
	}

	// Compose request with specified structue
	public static CompositeRequestBuilder compose(@Nonnull final ElementType first, @Nonnull final ElementType... others) {
		Assert.notNull(first, "Element type(s) must be specified");
		return new CompositeRequestBuilder(EnumSet.of(first, others));
	}

	// TODO non-present elements should serialize as empty arrays?  check behavious with DaData API.

	// Element value container. Can hold both single instances and arrays
	private static final class ElementValue<T> {
		@Getter(value = AccessLevel.PACKAGE, onMethod = @__({@JsonValue}))
		private final T value;

		ElementValue(final T value) {
			this.value = value;
		}

		static ElementValue of(final String... sources) {
			return sources.length == 1 ? new ElementValue<>(sources[0]) : new ElementValue<>(sources);
		}

		// for pretty-printing
		@Override
		public String toString() {
			return value instanceof Object[] ? Arrays.toString((Object[]) value) : value.toString();
		}
	}

	// Element builder
	@ToString
	public static class Element {
		private final Map<ElementType, ElementValue> elementContents = new EnumMap<>(ElementType.class);

		public Element asIs(@Nonnull String... sources) {
			return addElementContents(ElementType.AS_IS, sources);
		}

		public Element address(@Nonnull String... sources) {
			return addElementContents(ElementType.ADDRESS, sources);
		}

		public Element birthDate(@Nonnull String... sources) {
			return addElementContents(ElementType.BIRTHDATE, sources);
		}

		public Element email(@Nonnull String... sources) {
			return addElementContents(ElementType.EMAIL, sources);
		}

		public Element name(@Nonnull String... sources) {
			return addElementContents(ElementType.NAME, sources);
		}

		public Element passport(@Nonnull String... sources) {
			return addElementContents(ElementType.PASSPORT, sources);
		}

		public Element phone(@Nonnull String... sources) {
			return addElementContents(ElementType.PHONE, sources);
		}

		public Element vehicle(@Nonnull String... sources) {
			return addElementContents(ElementType.VEHICLE, sources);
		}

		public CompositeRequestBuilder and() {
			throw new UnsupportedOperationException("This method should be used only with builder-style composition");
		}

		@Nonnull
		Map<ElementType, ElementValue> getElementContents() {
			return Collections.unmodifiableMap(elementContents);
		}

		boolean nonEmpty() {
			return !elementContents.isEmpty();
		}

		private Element addElementContents(@Nonnull final ElementType elementType, final String... sources) {
			Assert.isTrue(sources != null && sources.length > 0, "Sources must be specified");
			elementContents.put(elementType, ElementValue.of(sources));
			return this;
		}

		// Should serialize without keys
		@SuppressWarnings("unused")
		@JsonValue
		private Collection<ElementValue> serialize() {
			return elementContents.values();
		}
	}

	// Composite request builder
	public static final class CompositeRequestBuilder {
		@Nonnull private CompositeRequest composite;


		// Construct builder for specified element types
		private CompositeRequestBuilder(@Nonnull final EnumSet<ElementType> elementTypes) {
			composite = new CompositeRequest(elementTypes);
		}

		// Construct element builder
		public ElementSpec element() {
			return new ElementSpec();
		}

		// Add elements to the request
		public CompositeRequestBuilder elements(@Nonnull final Element... elements) {
			// Check that elements conforms to the structure
			final String nonConforming = Arrays.stream(elements)
			                                   .map(Element::getElementContents)
			                                   .filter(elems -> !composite.elementTypes.containsAll(elems.keySet()))
			                                   .map(Object::toString)
			                                   .collect(Collectors.joining(", "));
			Assert.state(nonConforming.isEmpty(), "Elements not in the structure: " + nonConforming);
			composite.contents.addAll(Arrays.asList(elements));
			return this;
		}

		// Build the request
		public CompositeRequest build() {
			composite.stripEmptyElements();
			return composite;
		}

		// Element builder to be used in request builder
		public final class ElementSpec extends Element {

			@Override
			public CompositeRequestBuilder and() {
				Assert.state(nonEmpty(), "Element is empty");
				return elements(this);
			}
		}
	}
}
