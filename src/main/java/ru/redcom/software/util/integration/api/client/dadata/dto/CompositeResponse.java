/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.ToString;
import lombok.val;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.*;

/*
Ответ на запрос составной записи.
{
    "structure": [
        "AS_IS",
        "NAME",
        "ADDRESS",
        "PHONE"
    ],
    "data": [
        [{
            "source": "1"
        }, {
            "source": "Федотов Алексей",
            "result": "Федотов Алексей",
            ...
            "qc": 0
        }, {
            "source": "Москва, Сухонская улица, 11 кв 89",
            "result": "г Москва, ул Сухонская, д 11, кв 89",
            ...
            "qc": 0,
            "unparsed_parts": null
    }, {
            "source": "8 916 823 3454",
            "type": "Мобильный",
            "phone": "+7 916 823-34-54",
            ...
            "qc": 0
        }],
        [{
            "source": "2"
        }, {
            "source": "Иванов Сергей Владимирович",
            "result": "Иванов Сергей Владимирович",
            ...
            "qc": 0
        }, {
            "source": "мск,улица свободы,65,12",
            "result": "г Москва, ул Свободы, д 65, кв 12",
            ...
            "qc": 0,
            "unparsed_parts": null
        }, {
            "source": "495 663-12-53",
            "type": "Стационарный",
            "phone": "+7 495 663-12-53",
            ...
            "qc": 0
        }],
        [{
            "source": "3"
        }, {
            "source": "Ольга Павловна Ященко",
            "result": "Ященко Ольга Павловна",
            ...
            "qc": 0
        }, {
            "source": "Спб, ул Петрозаводская 8",
            "result": "Россия, г Санкт-Петербург, ул Петрозаводская, д 8",
            ...
            "qc": 0,
            "unparsed_parts": null
        }, {
            "source": "457 07 25",
            "type": "Стационарный",
            "phone": "+7 812 457-07-25",
            ...
            "qc": 1
        }]
    ]
}
 */
@ToString(of = {"structure", "data"})
@JsonDeserialize(using = CompositeResponse.CompositeResponseDeserializer.class)
public class CompositeResponse implements Iterable<CompositeResponse.Record> {
	public static final CompositeResponse EMPTY_RESPONSE = new CompositeResponse();

	// Structure
	@JsonProperty(required = true)
	@Nonnull
	private final Set<CompositeElementType> structure = EnumSet.noneOf(CompositeElementType.class);
	// Contents
	@JsonProperty
	@Nonnull
	private final List<Record> data = new LinkedList<>();


	private CompositeResponse() {
	}

	private CompositeResponse(@Nonnull final CompositeElementType[] structure) {
		this.structure.addAll(Arrays.asList(structure));
	}

	private void addRecord(@Nonnull final Record record) {
		data.add(record);
	}

	public Set<CompositeElementType> getStructure() {
		return Collections.unmodifiableSet(structure);
	}

	public List<Record> getData() {
		return Collections.unmodifiableList(data);
	}

	@Nonnull
	@Override
	public Iterator<Record> iterator() {
		return getData().iterator();
	}

	@Nonnull
	@Override
	public Spliterator<Record> spliterator() {
		return getData().spliterator();
	}


	@ToString
	public static class Record {
		@Nonnull private final Map<CompositeElementType, ResponseItem> items = new EnumMap<>(CompositeElementType.class);

		private void addItem(@Nonnull final CompositeElementType elementType, @Nullable final ResponseItem item) {
			items.put(elementType, item);
		}

		public boolean isEmpty() {
			return items.isEmpty();
		}

		@Nullable
		public ResponseItem get(@Nonnull final CompositeElementType elementType) {
			Assert.notNull(elementType, "Element type is null");
			return items.get(elementType);
		}

		@Nullable
		public AsIs getAsIs() {
			return (AsIs) get(CompositeElementType.AS_IS);
		}

		@Nullable
		public Address getAddress() {
			return (Address) get(CompositeElementType.ADDRESS);
		}

		@Nullable
		public BirthDate getBirthDate() {
			return (BirthDate) get(CompositeElementType.BIRTHDATE);
		}

		@Nullable
		public Email getEmail() {
			return (Email) get(CompositeElementType.EMAIL);
		}

		@Nullable
		public Name getName() {
			return (Name) get(CompositeElementType.NAME);
		}

		@Nullable
		public Passport getPassport() {
			return (Passport) get(CompositeElementType.PASSPORT);
		}

		@Nullable
		public Phone getPhone() {
			return (Phone) get(CompositeElementType.PHONE);
		}

		@Nullable
		public Vehicle getVehicle() {
			return (Vehicle) get(CompositeElementType.VEHICLE);
		}
	}


	// Composite response custom deserializer
	static class CompositeResponseDeserializer extends StdDeserializer<CompositeResponse> {

		CompositeResponseDeserializer() {
			super(CompositeResponse.class);
		}

		@Override
		public CompositeResponse deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
			val objectCodec = p.getCodec();
			final JsonNode treeNode = objectCodec.readTree(p);

			// parse the response structure
			CompositeElementType[] structure = {};
			final JsonNode structureNode = treeNode.path("structure");
			if (!(structureNode.isMissingNode() || structureNode.isNull())) {
				if (structureNode.isArray())
					structure = structureNode.traverse(objectCodec).readValueAs(structure.getClass());
				else
					ctxt.handleUnexpectedToken(structure.getClass(), structureNode.asToken(), p, "Structure descriptor element value '%s' is not an array", structureNode.toString());
			}
			if (structure.length == 0)
				throw MismatchedInputException.from(p, structure.getClass(), "Structure descriptor element is missing, null or empty");

			// nested arrays with polymorphic element at the 2nd level
			// element type is determined by structure item with index equal to element position in a record
			val response = new CompositeResponse(structure);
			final JsonNode dataNode = treeNode.path("data");
			if (dataNode.isArray()) {
				int recordNumber = 0;
				for (final JsonNode recordNode : dataNode) {
					if (recordNode.isArray()) {
						val record = new Record();
						int elementNumber = 0;
						for (final JsonNode elemNode : recordNode) {
							if (elementNumber >= structure.length)
								throw MismatchedInputException.from(p, ResponseItem[].class, "Element node #" + elementNumber + " of record #" + recordNumber + " is out of structure");
							val elementType = structure[elementNumber];
							record.addItem(elementType, elemNode.isNull() ? null : elemNode.traverse(objectCodec).readValueAs(elementType.getType()));
							elementNumber++;
						}
						response.addRecord(record);
					} else if (recordNode.isNull())
						ctxt.handleUnexpectedToken(ResponseItem[].class, recordNode.asToken(), p, "Record #%d node is null", recordNumber);
					else
						ctxt.handleUnexpectedToken(ResponseItem[].class, recordNode.asToken(), p, "Record #%d node value '%s' is not an array", recordNumber, recordNode.toString());
					recordNumber++;
				}
			} else if (dataNode.isMissingNode())
				throw MismatchedInputException.from(p, ResponseItem[][].class, "Data element is missing");
			else if (!dataNode.isNull())
				ctxt.handleUnexpectedToken(ResponseItem[][].class, dataNode.asToken(), p, "Data node value '%s' is not an array", dataNode.toString());

			return response;
		}
	}
}
