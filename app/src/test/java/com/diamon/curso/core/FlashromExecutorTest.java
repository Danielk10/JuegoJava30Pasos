package com.diamon.curso.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class FlashromExecutorTest {

    @Test
    public void testAmbiguityDetectionFromUserLog() {
        String log = "CH347 SPI clock set to 15MHz.\n" +
                "Found Winbond flash chip \"W25Q64BV/W25Q64CV/W25Q64FV\" (8192 kB, SPI) on ch347_spi.\n" +
                "Found Winbond flash chip \"W25Q64JV-.Q\" (8192 kB, SPI) on ch347_spi.\n" +
                "Multiple flash chip definitions match the detected chip(s): \"W25Q64BV/W25Q64CV/W25Q64FV\", \"W25Q64JV-.Q\"\n" +
                "Please specify which chip definition to use with the -c <chipname> option.\n";

        FlashromExecutor.AmbiguityInfo info = FlashromExecutor.parseOutputForAmbiguity(log);

        assertTrue("Should detect ambiguity", info.multipleChipsFound);
        assertEquals(2, info.suggestedChips.size());
        assertEquals("W25Q64BV/W25Q64CV/W25Q64FV", info.suggestedChips.get(0));
        assertEquals("W25Q64JV-.Q", info.suggestedChips.get(1));
    }

    @Test
    public void testSingleChipDetectedNotAmbiguous() {
        String log = "Found Spansion flash chip \"S25FL032P\" (4096 kB, SPI) on ch347_spi.\n" +
                "Reading flash... done.\n";

        FlashromExecutor.AmbiguityInfo info = FlashromExecutor.parseOutputForAmbiguity(log);

        assertFalse("Single chip should not trigger ambiguity", info.multipleChipsFound);
        assertTrue("Suggested chips should be empty", info.suggestedChips.isEmpty());
    }

    @Test
    public void testEmptyOrNormalLog() {
        String log = "flashrom v1.8.0-devel on Linux\n" +
                "No EEPROM/flash device found.\n";

        FlashromExecutor.AmbiguityInfo info = FlashromExecutor.parseOutputForAmbiguity(log);

        assertFalse(info.multipleChipsFound);
        assertTrue(info.suggestedChips.isEmpty());
    }
}
