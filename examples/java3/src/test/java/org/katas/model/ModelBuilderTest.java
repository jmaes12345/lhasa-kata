package org.katas.model;

import org.junit.jupiter.api.Test;

class ModelBuilderTest
{

	@Test
	void build() {
		new ModelBuilder().build();
	}

	@Test
	void readData() {
		var data = new ModelBuilder().readData();
		int i=0;
	}
}