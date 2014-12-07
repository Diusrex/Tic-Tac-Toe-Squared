package com.diusrex.tictactoe.logic.tests;

import org.junit.Test;

import com.diusrex.tictactoe.logic.BoxPosition;
import com.diusrex.tictactoe.logic.SectionPosition;

public class PositionTests {
    @Test
    public void testPositionToSection() {
        BoxPosition boxPos = new BoxPosition(0, 0);
        TestUtils.assertAreEqual(new SectionPosition(0, 0),
                boxPos.getSectionIn());

        boxPos = new BoxPosition(3, 8);
        TestUtils.assertAreEqual(new SectionPosition(1, 2),
                boxPos.getSectionIn());

        boxPos = new BoxPosition(5, 1);
        TestUtils.assertAreEqual(new SectionPosition(1, 0),
                boxPos.getSectionIn());
    }

    @Test
    public void testSectionToPosition() {
        SectionPosition sectionPos = new SectionPosition(0, 0);
        TestUtils.assertAreEqual(new BoxPosition(0, 0),
                sectionPos.getTopLeftPosition());

        sectionPos = new SectionPosition(1, 2);
        TestUtils.assertAreEqual(new BoxPosition(3, 6),
                sectionPos.getTopLeftPosition());
    }

}
