/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.lib.integration.api.client.dadata.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

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

/**
 * Composite request data transfer object.
 *
 * @author boris
 */
@ToString(of = {"structure", "data"})
@JsonPropertyOrder({"structure", "data"})
public class CompositeRequest {
	// Both structure and element contents are iterated in consistent ascending order of their key type ordinal
	// Structure of request (element types)
	@JsonProperty
	@NonNull
	private final Set<CompositeElementType> structure;
	@NonNull
	private final Set<CompositeElementType> unmodifiableStructure;
	private final boolean unconstrainedStructure;
	// Contents of request
	@JsonProperty
	@NonNull
	private final List<Record> data = new LinkedList<>();


	// Instance should be only constructed by builder
	private CompositeRequest() {
		this(EnumSet.noneOf(CompositeElementType.class));
	}

	private CompositeRequest(@NonNull final EnumSet<CompositeElementType> structure) {
		this.structure = structure;
		this.unmodifiableStructure = Collections.unmodifiableSet(this.structure);
		this.unconstrainedStructure = structure.isEmpty();
	}

	// Add element records from array to contents
	private void addRecords(@NonNull final Record[] records) {
		// adjust request structure with the new elements
		if (unconstrainedStructure)
			adjustStructure(records);
		else {
			// Check that elements conforms with the request structure
			final String nonConforming = checkConformance(records);
			Assert.state(nonConforming.isEmpty(), "Elements not in the structure: " + nonConforming);
		}
		// strip empty records out (can come from array-style building)
		data.addAll(Arrays.stream(records)
		                  .filter(Record::nonEmpty)
		                  .collect(Collectors.toList()));
	}

	// adjust request structure with the new elements
	private void adjustStructure(@NonNull final Record[] records) {
		structure.addAll(Arrays.stream(records)
		                       .map(Record::getStructure)
		                       .flatMap(Collection::stream)
		                       .collect(Collectors.toSet()));
	}

	// Check that records conforms to the request structure
	@NonNull
	private String checkConformance(@NonNull final Record[] records) {
		return Arrays.stream(records)
		             .filter(elem -> !elem.conformsWithStructure(unmodifiableStructure))
		             .map(Record::toStringBrief)
		             .collect(Collectors.joining(", "));
	}

	// Align records with request structure to fill gaps with nulls
	private void alignStructure() {
		data.forEach(record -> record.alignStructure(unmodifiableStructure));
	}

	/**
	 * Compose request with structure defined by payload.
	 *
	 * @return Composite request builder instance
	 */
	public static CompositeRequestBuilder compose() {
		return new CompositeRequestBuilder();
	}

	/**
	 * Compose request constrained to the specified structure.
	 *
	 * @param first First type structure element
	 * @param others    Other type structure elements
	 * @return Composite request builder instance
	 */
	public static CompositeRequestBuilder compose(@NonNull final CompositeElementType first, @NonNull final CompositeElementType... others) {
		Assert.notNull(first, "Element type(s) must be specified");
		return new CompositeRequestBuilder(EnumSet.of(first, others));
	}

	// Element value container. Can hold both single instances and arrays of Strings.
	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	private static final class ElementValue<T> {
		@JsonValue
		@Getter(value = AccessLevel.PACKAGE)
		@NonNull
		private final T value;

		@NonNull
		static ElementValue of(final String... sources) {
			return sources.length == 1 ? new ElementValue<>(sources[0]) : new ElementValue<>(sources);
		}

		// for pretty-printing
		@NonNull
		@Override
		public String toString() {
			return value instanceof Object[] ? Arrays.toString((Object[]) value) : value.toString();
		}
	}

	/**
	 * A record composed of request elements.
	 */
	@ToString
	public static class Record {
		@NonNull private final Map<CompositeElementType, ElementValue> recordElements = new EnumMap<>(CompositeElementType.class);

		/**
		 * Set As-Is request element source values.
		 *
		 * @param sources   Source values
		 * @return Record instance
		 */
		@NonNull
		public Record asIs(@NonNull String... sources) {
			return addElementContents(CompositeElementType.AS_IS, sources);
		}

		/**
		 * Set Address request element source values.
		 *
		 * @param sources   Source values
		 * @return Record instance
		 */
		@NonNull
		public Record address(@NonNull String... sources) {
			return addElementContents(CompositeElementType.ADDRESS, sources);
		}

		/**
		 * Set Birthdate request element source values.
		 *
		 * @param sources   Source values
		 * @return Record instance
		 */
		@NonNull
		public Record birthDate(@NonNull String... sources) {
			return addElementContents(CompositeElementType.BIRTHDATE, sources);
		}

		/**
		 * Set Email request element source values.
		 *
		 * @param sources   Source values
		 * @return Record instance
		 */
		@NonNull
		public Record email(@NonNull String... sources) {
			return addElementContents(CompositeElementType.EMAIL, sources);
		}

		/**
		 * Set Name request element source values.
		 *
		 * @param sources   Source values
		 * @return Record instance
		 */
		@NonNull
		public Record name(@NonNull String... sources) {
			return addElementContents(CompositeElementType.NAME, sources);
		}

		/**
		 * Set Passport request element source values.
		 *
		 * @param sources   Source values
		 * @return Record instance
		 */
		@NonNull
		public Record passport(@NonNull String... sources) {
			return addElementContents(CompositeElementType.PASSPORT, sources);
		}

		/**
		 * Set Phone Number request element source values.
		 *
		 * @param sources   Source values
		 * @return Record instance
		 */
		@NonNull
		public Record phone(@NonNull String... sources) {
			return addElementContents(CompositeElementType.PHONE, sources);
		}

		/**
		 * Set Vehicle Type request element source values.
		 *
		 * @param sources   Source values
		 * @return Record instance
		 */
		@NonNull
		public Record vehicle(@NonNull String... sources) {
			return addElementContents(CompositeElementType.VEHICLE, sources);
		}

		// This method should only be used with builder-style
		public CompositeRequestBuilder and() {
			throw new UnsupportedOperationException("This method should be used only with builder-style composition");
		}

		// Check if record contents is not empty
		boolean nonEmpty() {
			return !recordElements.isEmpty();
		}

		// Check if record contents conforms with the request structure
		boolean conformsWithStructure(@NonNull final Set<CompositeElementType> structure) {
			return structure.containsAll(recordElements.keySet());
		}

		// Align record contents to the request structure by filling gaps with null values
		void alignStructure(@NonNull final Set<CompositeElementType> structure) {
			structure.forEach(e -> recordElements.putIfAbsent(e, null));
		}

		// Get the record structure
		Set<CompositeElementType> getStructure() {
			return Collections.unmodifiableSet(recordElements.keySet());
		}

		// Store a contents
		@NonNull
		private Record addElementContents(@NonNull final CompositeElementType elementType, final String... sources) {
			Assert.isTrue(sources != null && sources.length > 0, "Sources must be specified");
			recordElements.put(elementType, ElementValue.of(sources));
			return this;
		}

		// Should serialize without keys
		@SuppressWarnings("unused")
		@NonNull
		@JsonValue
		private Collection<ElementValue> serialize() {
			return recordElements.values();
		}

		// for non-conformant elements exception message
		@NonNull
		private String toStringBrief() {
			return recordElements.toString();
		}
	}

	/**
	 * Composite request builder.
	 */
	public static final class CompositeRequestBuilder {
		@NonNull private final CompositeRequest composite;


		// Compose request with structure defined by payload
		private CompositeRequestBuilder() {
			composite = new CompositeRequest();
		}

		// Construct builder for specified element types
		private CompositeRequestBuilder(@NonNull final EnumSet<CompositeElementType> structure) {
			composite = new CompositeRequest(structure);
		}

		/**
		 * Construct a record builder.
		 *
		 * @return Record builder instance
		 */
		@NonNull
		public RecordSpec record() {
			return new RecordSpec();
		}

		/**
		 * <p>Add records to the request.</P>
		 * Used in array-style composing.
		 *
		 * @param records  Records array
		 * @return Composite Request builder instance
		 */
		@NonNull
		public CompositeRequestBuilder records(@NonNull final Record... records) {
			Assert.notNull(records, "Records cannot be null");
			composite.addRecords(records);
			return this;
		}

		/**
		 * Finally build the request.
		 *
		 * @return Composite Request instance
		 */
		@NonNull
		public CompositeRequest build() {
			composite.alignStructure();
			return composite;
		}

		/**
		 * Record builder class to be used in request builder.
		 */
		public final class RecordSpec extends Record {

			/**
			 * Finalize the record construction and add it to the request contents.
			 * Prepare to construct a new record or build the entire request.
			 *
			 * @return Composite Request builder instance
			 */
			@NonNull
			@Override
			public CompositeRequestBuilder and() {
				Assert.state(nonEmpty(), "Record contains no elements");
				return records(this);
			}
		}
	}
}
