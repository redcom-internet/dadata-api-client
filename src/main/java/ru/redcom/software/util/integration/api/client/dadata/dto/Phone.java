/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.redcom.software.util.integration.api.client.dadata.types.QcConflict;
import ru.redcom.software.util.integration.api.client.dadata.types.QcPhone;

/*
Название	Длина	Описание
source	100	Исходный телефон одной строкой
type	50	Тип телефона
phone	50	Стандартизованный телефон одной строкой
country_code	5	Код страны
city_code	5	Код города / DEF-код
number	10	Локальный номер телефона
extension	10	Добавочный номер
provider	100	Оператор связи
region	100	Регион
timezone	10	Часовой пояс
qc_conflict	5	Признак конфликта телефона с адресом
qc	5	Код проверки

[
  {
    "source": "тел 7165219 доб139",
    "type": "Стационарный",
    "phone": "+7 495 716-52-19 доб. 139",
    "country_code": "7",
    "city_code": "495",
    "number": "7165219",
    "extension": "139",
    "provider": "ОАО \"МГТС\"",
    "region": "Москва",
    "timezone": "UTC+3",
    "qc_conflict": 0,
    "qc": 1
  }
]
*/

/**
 * Cleaned Phone Number container.
 */
@SuppressWarnings("unused")
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Phone extends ResponseItem {
	private String type;
	private String phone;

	@JsonProperty("country_code")
	private String countryCode;
	@JsonProperty("city_code")
	private String cityCode;
	private String number;
	private String extension;

	private String provider;
	private String region;

	private String timezone;

	@JsonProperty("qc_conflict")
	private QcConflict qcConflict;
	@JsonProperty(required = true)
	private QcPhone qc;

	// This class is not instantiable
	private Phone() {
	}
}
