package com.project.bumawiki.domain.docs.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class VersionDocsPk implements Serializable {
	private int version;
	private Docs docs;
}
