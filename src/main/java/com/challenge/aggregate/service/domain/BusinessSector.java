package com.challenge.aggregate.service.domain;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BusinessSector implements Serializable {
	private static final long serialVersionUID = -710436490751362312L;

	private String number;
	private String sector;
}