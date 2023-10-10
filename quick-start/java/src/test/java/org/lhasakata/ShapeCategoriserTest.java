package org.lhasakata;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Copyright © 2023 Lhasa Limited
 * File created: 11/10/2023 by JamesB
 * Creator : JamesB
 * Version : $$Id$$
 */
public class ShapeCategoriserTest
{
	@Test
	public void getCategory_FirstTest_ToBeDeleted()
	{
		//TODO replace me with a real test
		int value = ShapeCategoriser.calculateCategory(null);
		assertEquals(-1, value);
	}
}