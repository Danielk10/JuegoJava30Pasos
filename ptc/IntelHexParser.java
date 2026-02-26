package com.diamon.ptc;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Parser simple para archivos Intel HEX.
 * Extrae los datos binarios organizados por dirección.
 */
public class IntelHexParser {

    public static class HexRecord {
        public int address;
        public byte[] data;

        public HexRecord(int address, byte[] data) {
            this.address = address;
            this.data = data;
        }
    }

    /**
     * Parsear el contenido de un archivo HEX y devolver un mapa de dirección ->
     * datos.
     */
    public static TreeMap<Integer, Byte> parse(String hexContent) {
        TreeMap<Integer, Byte> memory = new TreeMap<>();
        String[] lines = hexContent.split("\n");
        int extendedAddress = 0;

        for (String line : lines) {
            line = line.trim();
            if (!line.startsWith(":") || line.length() < 11)
                continue;

            try {
                int byteCount = Integer.parseInt(line.substring(1, 3), 16);
                int address = Integer.parseInt(line.substring(3, 7), 16);
                int recordType = Integer.parseInt(line.substring(7, 9), 16);

                if (recordType == 0) { // Data Record
                    int fullAddress = extendedAddress + address;
                    for (int i = 0; i < byteCount; i++) {
                        int start = 9 + (i * 2);
                        byte data = (byte) Integer.parseInt(line.substring(start, start + 2), 16);
                        memory.put(fullAddress + i, data);
                    }
                } else if (recordType == 4) { // Extended Linear Address Record
                    extendedAddress = Integer.parseInt(line.substring(9, 13), 16) << 16;
                } else if (recordType == 2) { // Extended Segment Address Record
                    extendedAddress = Integer.parseInt(line.substring(9, 13), 16) << 4;
                }
            } catch (Exception e) {
                // Saltar línea malformada
            }
        }
        return memory;
    }
}
