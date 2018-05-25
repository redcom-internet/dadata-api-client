/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import lombok.*;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@ToString
@JsonDeserialize(using = CompositeResponse.CompositeResponseDeserializer.class)
public class CompositeResponse {
	// Structure
	@JsonProperty(required = true)
	@Nonnull
	private final CompositeElementType[] structure;
	// Contents
	// 1st level: records
	// 2nd level: elements within record according to structure
	@JsonProperty
	@Nonnull
	private final CompositeElement[][] data;


/*
	@RequiredArgsConstructor
	static class CompositeElementConverter extends StdConverter<JsonNode, CompositeElement> {

		@Nonnull private final ObjectMapper objectMapper;

		@Override
		public CompositeElement convert(final JsonNode value) {
			System.out.println("node = " + value);
			try {
				System.out.println("node value = " + objectMapper.readValue(value.traverse(), AsIs.class));
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
*/

//	static class CompositeElementTypeIdResolver extends TypeIdResolverBase {
//
//		@Override
//		public JsonTypeInfo.Id getMechanism() {
//			return JsonTypeInfo.Id.CUSTOM;
//		}
//
//		@Override
//		public void init(final JavaType bt) {
//			super.init(bt);
//			System.out.println("init with " + bt);
//		}
//
//		// Serialize?
//		@Override
//		public String idFromValue(final Object value) {
//			return value.getClass().getName();
//		}
//
//		// Serialize?
//		@Override
//		public String idFromValueAndType(final Object value, final Class<?> suggestedType) {
//			return value.getClass().getName();
//		}
//
//		// Deserialize ?
//		@Override
//		public JavaType typeFromId(final DatabindContext context, final String id) throws IOException {
//			System.out.println("type from id '" + id + "'");
//			return context.constructType(AsIs.class);
//
//		}
//	}

//	static class CompositeElementDeserializer extends StdDeserializer<CompositeElement> {
//
//		CompositeElementDeserializer() {
//			super(CompositeElement.class);
//		}
//
//		@Override
//		public CompositeElement deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException, JsonProcessingException {
//			System.out.println("current name: " + p.currentName());
//			System.out.println("current token: " + p.currentToken());
//			System.out.println("current token id: " + p.currentTokenId());
//			return null;
//		}
//	}
//
//	static class CompositeElementDeserializer extends PresentPropertyPolymorphicDeserializer<CompositeElement> {
//		public CompositeElementDeserializer() {
//		super(CompositeElement.class);
//	}
//	}

	static class CompositeResponseDeserializer extends StdDeserializer<CompositeResponse> {

		CompositeResponseDeserializer() {
			super(CompositeResponse.class);
		}

		@Override
		public CompositeResponse deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
			val treeNode = p.getCodec().readTree(p);
			System.out.println("tree node = " + treeNode);

			CompositeElementType[] structure = {};
			val structureNode = treeNode.get("structure");
			System.out.println("structure node = " + structureNode);
			if (structureNode.isArray() && structureNode.size() > 0) {
				val structureIter = structureNode.traverse(p.getCodec()).readValuesAs(CompositeElementType[].class);
				if (structureIter.hasNext())
					structure = structureIter.next();
			}
			System.out.println("structure = " + Arrays.toString(structure));
			// TODO fail if structure is empty

			// nested arrays with element types of 2nd level is determined by structure item with index equal to element position in array

			final List<List<CompositeElement>> dataList = new ArrayList<>();
			val dataNode = treeNode.get("data");
			System.out.println("data node = " + dataNode);
			if (dataNode.isArray()) {
				dataNode.traverse(p.getCodec()).readValuesAs(Collection.class).forEachRemaining(l1e -> {
					System.out.println("L1 = " + l1e);
				});
				for (int i = 0; i < dataNode.size(); i++) {
					val recordNode = dataNode.get(i);
					if (recordNode.isArray()) {
						recordNode.traverse(p.getCodec()).readValuesAs(Collection.class).forEachRemaining(l2e -> {
							System.out.println("L2 = " + l2e);
						});

						final List<CompositeElement> recordList = new ArrayList<>();
						for (int j = 0; j < recordNode.size(); j++) {
							val elemNode = recordNode.get(j);
							System.out.println("elemNode = " + elemNode);
							// TODO fail if j is out of structure array
							//if (j >= structure.length)
							//	throw new ... Element i/j is out of response structure
							val elemTypeName = structure[j].name();
							val elemType = structure[j].getType();
							System.out.println("elem " + elemTypeName + " type " + elemType);
							if (elemNode.isObject()) {
								System.out.println("element is object");
								val elemIter = elemNode.traverse(p.getCodec()).readValuesAs(elemType);
								if (elemIter.hasNext()) {
									val elem = elemIter.next();
									System.out.println("elem = " + elem);
									recordList.add(elem);
								} // todo else fail
							}  // todo else fail
						}
						dataList.add(recordList);
					} // todo else fail
				}
			} // todo else fail
			// todo convert data to double-sized array and construct response object
			System.out.println("data list = " + dataList);

			final CompositeElement[][] data = dataList.stream()
			                                          .map(l -> l.toArray(new CompositeElement[0]))
			                                          .toArray(CompositeElement[][]::new);
			System.out.println("data = " + Arrays.deepToString(data));

			return new CompositeResponse(structure, data);
		}
	}
}
