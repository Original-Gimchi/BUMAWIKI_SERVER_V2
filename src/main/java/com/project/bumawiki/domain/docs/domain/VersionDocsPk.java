package com.project.bumawiki.domain.docs.domain;

import java.io.Serializable;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class VersionDocsPk implements Serializable {
	private Integer version;
	private Long docs;
}
