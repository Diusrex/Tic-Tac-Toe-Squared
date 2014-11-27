package com.diusrex.tictactoe.tests;

import junit.framework.Assert;

import com.diusrex.tictactoe.BoxPosition;
import com.diusrex.tictactoe.SectionPosition;

public class TestUtils {
    public static void assertAreEqual(SectionPosition expected, SectionPosition actual) {
        Assert.assertEquals(expected.getX(), actual.getX());
        Assert.assertEquals(expected.getY(), actual.getY());
    }
    
    public static void assertAreEqual(BoxPosition expected, BoxPosition actual) {
        Assert.assertEquals(expected.getX(), actual.getX());
        Assert.assertEquals(expected.getY(), actual.getY());
    }
}
