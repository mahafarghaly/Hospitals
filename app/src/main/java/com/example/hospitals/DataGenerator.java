package com.example.hospitals;

import java.util.ArrayList;

public class DataGenerator {
    private static final String[] hospitalNames = {
            "Andalusia Maadi Hospital",
            "As-Salam International Hospital",
            "Saudi German Hospital, Cairo",
            "Elite Hospital, Alexandria",
            "Nile Hospital, Hurghada",
            "aseel medical care hospital",
            "South Sinai Hospital, Sharm El-Sheikh",
            "Al Madinah Specialized Hospital, Luxor",
            "Khanka Mental Health Hospital",
            "Dar Al Fouad Hospital"
    };
    private static final String[] descriptions = {
            "andalusia hospital al maadi , located in Maadi, Cairo, offers comprehensive medical services with advanced equipment and skilled professionals, ensuring quality healthcare in a compassionate environment.",
            "As-Salam International Hospital is a leading healthcare facility known for its advanced medical services and compassionate patient care.",
            "Saudi German Hospital in Cairo is a renowned healthcare institution offering high-quality medical services and patient-centered care.",
            "Elite Hospital in Alexandria is a leading medical facility known for its exceptional healthcare services and commitment to patient well-being.",
            "Nile Hospital in Hurghada is a trusted healthcare institution providing quality medical services to the local community and visitors.",
            "Aseel Medical Care Hospital is a leading healthcare facility known for its exceptional medical services and patient-centered care.",
            "South Sinai Hospital in Sharm El-Sheikh is a renowned medical center providing quality healthcare services and specialized treatments to residents and tourists in the region.",
            "Al Madinah Specialized Hospital in Luxor is a leading healthcare facility offering specialized medical services and advanced treatments to patients in Luxor and surrounding areas.",
            "Khanka Mental Health Hospital is a specialized healthcare facility dedicated to providing mental health services, support, and treatments to individuals dealing with various mental health conditions and disorders.",
            "Dar Al Fouad Hospital is a leading healthcare institution known for its advanced medical services and patient-centric care, catering to a wide range of medical specialties and treatments with a focus on quality and excellence."
    };
    private static final String[] phoneNumbers = {
            "16781",
            "01092001443",
            "16259",
            "0315461",
            "0653550974",
            "01111107006",
            "01220003533",
            "01274666633",
            "0244698987",
            "01122211190"
    };
    private static final double[] latitudes = {
            //30.07062573681156, 31.319250081836653
            29.9692,
            29.985374056048748,
            30.133047989471653,
            31.171920636281392,
            27.217248531379415,
            27.20543347081496,
            27.868586774084292,
            25.687166779977623,
            30.223956477378582,
            30.07062573681156
    };
    private static final double[] longitudes = {
            31.2463,
            31.230265084655752,
            31.384420169456725,
            29.943512840228866,
            33.81781491055436,
            33.83883399640607,
            34.299427339418784,
            32.63309252398207,
            31.367921124313234,
            31.319250081836653
    };

    public static ArrayList<HospitalItem> generateFakeData() {
        ArrayList<HospitalItem> hospitalList = new ArrayList<>();

        for (int i = 0; i < hospitalNames.length; i++) {
            int imageResource;
            if (i == 0) {
                imageResource = R.drawable.img1;
            } else if (i == 1) {
                imageResource = R.drawable.img2;
            } else if (i == 2) {
                imageResource = R.drawable.img3;
            } else if (i == 3) {
                imageResource = R.drawable.img4;
            } else if (i == 4) {
                imageResource = R.drawable.img5;
            } else if (i == 5) {
                imageResource = R.drawable.img6;
            } else if (i == 6) {
                imageResource = R.drawable.img7;
            } else if (i == 7) {
                imageResource = R.drawable.img8;
            } else if (i == 8) {
                imageResource = R.drawable.img1;
            } else if (i == 9) {
                imageResource = R.drawable.img10;
            } else {
                imageResource = R.drawable.img;
            }
            String name = hospitalNames[i];
            String description = descriptions[i];
            String phoneNumber = phoneNumbers[i];
            double latitude = latitudes[i];
            double longitude = longitudes[i];

            HospitalItem hospitalItem = new HospitalItem(imageResource, name, description, phoneNumber, latitude, longitude);
            hospitalList.add(hospitalItem);
        }

        return hospitalList;
    }
}
