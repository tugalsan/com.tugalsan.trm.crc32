package com.tugalsan.trm.trainer;

import com.tugalsan.api.desktop.server.TS_DesktopUtils;
import com.tugalsan.api.file.server.TS_DirectoryUtils;
import com.tugalsan.api.file.server.TS_FileUtils;
import com.tugalsan.api.file.xlsx.server.TS_FileXlsxTable;

public class Main {

    public static void main(String... args) {
        var folder = TS_DesktopUtils.chooseFolder();
        if (folder == null) {
            TS_DesktopUtils.info("folder == null -> exit;");
            return;
        }
        var output = folder.resolve("crc32.xlsx");

        var xlsxTable = TS_FileXlsxTable.ofXlsx();
        xlsxTable.setValue(xlsxTable.getRowSize(), "DOSYA", "CRC32", "OLUŞTURMA", "GÜNCELLEME");
        TS_DirectoryUtils
                .subFiles(folder, "*.*", true, true)
                .forEach(file -> {
                    xlsxTable.setValue(
                            xlsxTable.getRowSize(),
                            file.toString(),
                            TS_FileUtils.getChecksumHex(file),
                            TS_FileUtils.getTimeCreationTime(file).toString_dateOnly(),
                            TS_FileUtils.getTimeLastModified(file).toString_dateOnly()
                    );
                });
        xlsxTable.toFile(output);

        TS_DesktopUtils.info("See " + output.toString());
    }
}
