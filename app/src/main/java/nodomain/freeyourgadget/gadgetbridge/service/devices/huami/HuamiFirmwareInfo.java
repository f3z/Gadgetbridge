/*  Copyright (C) 2017 Andreas Shimokawa

    This file is part of Gadgetbridge.

    Gadgetbridge is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Gadgetbridge is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>. */

package nodomain.freeyourgadget.gadgetbridge.service.devices.huami;

import java.util.Map;

import nodomain.freeyourgadget.gadgetbridge.impl.GBDevice;
import nodomain.freeyourgadget.gadgetbridge.util.ArrayUtils;
import nodomain.freeyourgadget.gadgetbridge.util.CheckSums;


public abstract class HuamiFirmwareInfo {

    protected static final byte[] RES_HEADER = new byte[]{ // HMRES resources file (*.res)
            0x48, 0x4d, 0x52, 0x45, 0x53
    };

    protected static final byte[] FT_HEADER = new byte[]{ // HMZK font file (*.ft, *.ft.xx)
            0x48,
            0x4d,
            0x5a,
            0x4b
    };

    private HuamiFirmwareType firmwareType = HuamiFirmwareType.FIRMWARE;

    public String toVersion(int crc16) {
        return getCrcMap().get(crc16);
    }

    public int[] getWhitelistedVersions() {
        return ArrayUtils.toIntArray(getCrcMap().keySet());
    }

    private final int crc16;

    private byte[] bytes;

    private String firmwareVersion;

    public HuamiFirmwareInfo(byte[] bytes) {
        this.bytes = bytes;
        crc16 = CheckSums.getCRC16(bytes);
        firmwareVersion = getCrcMap().get(crc16);
        firmwareType = determineFirmwareType(bytes);
    }

    public abstract boolean isGenerallyCompatibleWith(GBDevice device);

    public boolean isHeaderValid() {
        return getFirmwareType() != HuamiFirmwareType.INVALID;
    }

    public void checkValid() throws IllegalArgumentException {
    }

    /**
     * Returns the size of the firmware in number of bytes.
     *
     * @return
     */
    public int getSize() {
        return bytes.length;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public int getCrc16() {
        return crc16;
    }

    public int getFirmwareVersion() {
        return getCrc16(); // HACK until we know how to determine the version from the fw bytes
    }

    public HuamiFirmwareType getFirmwareType() {
        return firmwareType;
    }

    protected abstract Map<Integer, String> getCrcMap();

    protected abstract HuamiFirmwareType determineFirmwareType(byte[] bytes);
}