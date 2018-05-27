/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
@ToString(of = {"structure", "data"})
@JsonPropertyOrder({"structure", "data"})
public class CompositeRequest {

	// These collections are iterated in consistent ascending order of their key type ordinal
	// Structure of request (element types)
	@JsonProperty
	@Nonnull
	private final Set<CompositeElementType> structure;
	@Nonnull
	private final Set<CompositeElementType> unmodifiableStructure;
	private final boolean unconstrainedStructure;
	// Contents of request
	@JsonProperty
	@Nonnull
	private final List<Element> data = new LinkedList<>();


	// Instance should be only constructed by builder
	private CompositeRequest() {
		this(EnumSet.noneOf(CompositeElementType.class));
	}

	private CompositeRequest(@Nonnull final EnumSet<CompositeElementType> structure) {
		this.structure = structure;
		this.unmodifiableStructure = Collections.unmodifiableSet(this.structure);
		this.unconstrainedStructure = structure.isEmpty();
	}

	// Add elements from array to contents
	private void addElements(@Nonnull final Element[] elements) {
		// adjust request structure with the new elements
		if (unconstrainedStructure)
			adjustStructure(elements);
		else {
			// Check that elements conforms with the request structure
			final String nonConforming = checkConformance(elements);
			Assert.state(nonConforming.isEmpty(), "Elements not in the structure: " + nonConforming);
		}
		// strip empty elements out (can come from array-style building)
		data.addAll(Arrays.stream(elements)
		                  .filter(Element::nonEmpty)
		                  .collect(Collectors.toList()));
	}

	// adjust request structure with the new elements
	private void adjustStructure(@Nonnull final Element[] elements) {
		structure.addAll(Arrays.stream(elements)
		                       .map(Element::getStructure)
		                       .flatMap(Collection::stream)
		                       .collect(Collectors.toSet()));
	}

	// Check that elements conforms to the request structure
	@Nonnull
	private String checkConformance(@Nonnull final Element[] elements) {
		return Arrays.stream(elements)
		             .filter(elem -> !elem.conformsWithStructure(unmodifiableStructure))
		             .map(Element::toStringBrief)
		             .collect(Collectors.joining(", "));
	}

	// Align elements with request structure to fill gaps with nulls
	private void alignStructure() {
		data.forEach(element -> element.alignStructure(unmodifiableStructure));
	}

	// Compose request with structure defined by payload
	public static CompositeRequestBuilder compose() {
		return new CompositeRequestBuilder();
	}

	// Compose request constrained to the specified structure
	public static CompositeRequestBuilder compose(@Nonnull final CompositeElementType first, @Nonnull final CompositeElementType... others) {
		Assert.notNull(first, "Element type(s) must be specified");
		return new CompositeRequestBuilder(EnumSet.of(first, others));
	}

	// Element value container. Can hold both single instances and arrays, as well as null reference to indicate element absence
	private static final class ElementValue<T> {
		@Getter(value = AccessLevel.PACKAGE, onMethod = @__({@JsonValue}))
		private final T value;

		ElementValue(final T value) {
			this.value = value;
		}

		@Nonnull
		static ElementValue of(final String... sources) {
			return sources.length == 1 ? new ElementValue<>(sources[0]) : new ElementValue<>(sources);
		}

		// for pretty-printing
		@Nonnull
		@Override
		public String toString() {
			return value instanceof Object[] ? Arrays.toString((Object[]) value) : value.toString();
		}
	}

	// Element builder
	@ToString
	public static class Element {
		private final Map<CompositeElementType, ElementValue> elementContents = new EnumMap<>(CompositeElementType.class);

		@Nonnull
		public Element asIs(@Nonnull String... sources) {
			return addElementContents(CompositeElementType.AS_IS, sources);
		}

		@Nonnull
		public Element address(@Nonnull String... sources) {
			return addElementContents(CompositeElementType.ADDRESS, sources);
		}

		@Nonnull
		public Element birthDate(@Nonnull String... sources) {
			return addElementContents(CompositeElementType.BIRTHDATE, sources);
		}

		@Nonnull
		public Element email(@Nonnull String... sources) {
			return addElementContents(CompositeElementType.EMAIL, sources);
		}

		@Nonnull
		public Element name(@Nonnull String... sources) {
			return addElementContents(CompositeElementType.NAME, sources);
		}

		@Nonnull
		public Element passport(@Nonnull String... sources) {
			return addElementContents(CompositeElementType.PASSPORT, sources);
		}

		@Nonnull
		public Element phone(@Nonnull String... sources) {
			return addElementContents(CompositeElementType.PHONE, sources);
		}

		@Nonnull
		public Element vehicle(@Nonnull String... sources) {
			return addElementContents(CompositeElementType.VEHICLE, sources);
		}

		public CompositeRequestBuilder and() {
			throw new UnsupportedOperationException("This method should be used only with builder-style composition");
		}

		boolean nonEmpty() {
			return !elementContents.isEmpty();
		}

		// Check if element contents conforms with the request structure
		boolean conformsWithStructure(@Nonnull final Set<CompositeElementType> structure) {
			return structure.containsAll(elementContents.keySet());
		}

		// Align element contents to the request structure by filling gaps with null values
		void alignStructure(@Nonnull final Set<CompositeElementType> structure) {
			structure.forEach(e -> elementContents.putIfAbsent(e, null));
		}

		// Get the element structure
		Set<CompositeElementType> getStructure() {
			return Collections.unmodifiableSet(elementContents.keySet());
		}

		@Nonnull
		private Element addElementContents(@Nonnull final CompositeElementType elementType, final String... sources) {
			Assert.isTrue(sources != null && sources.length > 0, "Sources must be specified");
			elementContents.put(elementType, ElementValue.of(sources));
			return this;
		}

		// Should serialize without keys
		@SuppressWarnings("unused")
		@Nonnull
		@JsonValue
		private Collection<ElementValue> serialize() {
			return elementContents.values();
		}

		// for non-conformant elements exception message
		@Nonnull
		private String toStringBrief() {
			return elementContents.toString();
		}
	}

	// Composite request builder
	public static final class CompositeRequestBuilder {
		@Nonnull private CompositeRequest composite;


		// Compose request with structure defined by payload
		private CompositeRequestBuilder() {
			composite = new CompositeRequest();
		}

		// Construct builder for specified element types
		private CompositeRequestBuilder(@Nonnull final EnumSet<CompositeElementType> structure) {
			composite = new CompositeRequest(structure);
		}

		// Construct element builder
		@Nonnull
		public ElementSpec element() {
			return new ElementSpec();
		}

		// Add elements to the request
		@Nonnull
		public CompositeRequestBuilder elements(@Nonnull final Element... elements) {
			Assert.notNull(elements, "Elements cannot be null");
			composite.addElements(elements);
			return this;
		}

		// Build the request
		@Nonnull
		public CompositeRequest build() {
			composite.alignStructure();
			return composite;
		}

		// Element builder to be used in request builder
		public final class ElementSpec extends Element {

			@Nonnull
			@Override
			public CompositeRequestBuilder and() {
				Assert.state(nonEmpty(), "Element is empty");
				return elements(this);
			}
		}
	}
}
