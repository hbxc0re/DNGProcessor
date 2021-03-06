package amirz.dngprocessor.device;

import android.util.SparseArray;

import amirz.dngprocessor.params.ProcessParams;
import amirz.dngprocessor.params.SensorParams;
import amirz.dngprocessor.parser.TIFF;
import amirz.dngprocessor.parser.TIFFTag;

import static amirz.dngprocessor.Constants.PLUS;

public class MotoG6 extends Generic {
    @Override
    public boolean isModel(String model) {
        return model.startsWith("moto g(6)");
    }

    @Override
    public void sensorCorrection(SparseArray<TIFFTag> tags, SensorParams sensor) {
        super.sensorCorrection(tags, sensor);

        if (sensor.gainMap == null) {
            // Extracted from a Camera2 photo
            sensor.gainMap = d2f(
                    2.7091816625, 2.454971925, 2.1506592625, 1.9805712624999998, 1.82734355, 1.72615125, 1.6894398, 1.72301305, 1.8291785, 1.9754675750000001, 2.156523375, 2.4733261499999997, 2.7608015874999996,
                    2.5475299625, 2.1676563499999997, 1.9369760500000002, 1.7285031499999999, 1.559904775, 1.4656663250000002, 1.436121125, 1.4639520125, 1.5537302, 1.7218862874999998, 1.9320019000000002, 2.168759525, 2.5633632499999996,
                    2.3285123499999996, 2.018356525, 1.7648059625, 1.5328743999999999, 1.3908445375, 1.3060006875, 1.27425725, 1.3030814375, 1.3844899125, 1.52193545, 1.7548983875000002, 2.0037337375, 2.3349842,
                    2.204470975, 1.9172350624999999, 1.6325386375000002, 1.4229334999999999, 1.2803529625, 1.1685853000000002, 1.125249575, 1.1605595750000002, 1.2682481625, 1.4085095250000002, 1.6162260000000002, 1.89631545, 2.1930648625,
                    2.156538975, 1.8597040374999998, 1.56833175, 1.3714116625, 1.2100522999999999, 1.087551225, 1.0379170875, 1.0745168875, 1.194615475, 1.354820975, 1.5485908625000002, 1.8332039375000002, 2.1250934249999998,
                    2.1508892875, 1.859270725, 1.5657657999999999, 1.3697351125000001, 1.2089787125, 1.0862922625, 1.0381207, 1.07642125, 1.195305925, 1.356258375, 1.5466158624999997, 1.82905485, 2.1194305249999994,
                    2.2050355625, 1.91479135, 1.6255247500000003, 1.4185947875, 1.277153025, 1.165422425, 1.1231096999999999, 1.1591060375, 1.267472675, 1.4074006625, 1.6074348999999999, 1.8833028125, 2.1737473625,
                    2.3236944125, 2.011578975, 1.7546635, 1.5243631750000002, 1.3870425374999997, 1.3024955249999999, 1.2688906625, 1.2979457, 1.3784740875, 1.5121629250000002, 1.7376604, 1.9855739374999999, 2.3107723124999997,
                    2.5196864875000005, 2.1556872, 1.9250188625, 1.7114033375, 1.5443060625, 1.4549411125000002, 1.4265024125, 1.4510423375, 1.53533655, 1.6979879125, 1.9084106624999997, 2.1404821000000003, 2.5092960375,
                    2.6976679, 2.4191333499999996, 2.1371014, 1.9622389875, 1.8121170500000001, 1.7011511125, 1.6653431624999997, 1.6973457125, 1.7960343625, 1.9418083874999998, 2.11749155, 2.4192555625, 2.7465464624999996
            );
            sensor.gainMapSize = new int[] { 13, 10 };
        }

        // Dot-fix
        int w = 16;
        int h = 16;

        sensor.hotPixelsSize = new int[] { w, h };
        sensor.hotPixels = new short[w * h];

        sensor.hotPixels[2 * w + 14] = PLUS;
        sensor.hotPixels[6 * w + 14] = PLUS;
        sensor.hotPixels[10 * w + 6] = PLUS;
        sensor.hotPixels[14 * w + 6] = PLUS;
    }

    @Override
    public void processCorrection(SparseArray<TIFFTag> tags, ProcessParams process) {
        TIFFTag software = tags.get(TIFF.TAG_Software);
        if (software != null && software.toString().contains("HDR+")) {
            float[] saturationMap = process.saturationMap;
            saturationMap[0] *= 2.31f;
            saturationMap[1] *= 2.63f;
            saturationMap[2] *= 3.15f;
            saturationMap[3] *= 2.1f;
            saturationMap[4] *= 2f;
            saturationMap[5] *= 2f;
            saturationMap[6] *= 2.52f;
            saturationMap[7] *= 2.31f;
        } else {
            super.processCorrection(tags, process);
        }
    }
}
