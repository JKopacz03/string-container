package org.kopacz;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        StringContainer st = new StringContainer("\\d{2}[-]\\d{3}");

        st.add("02-495");//git
        st.add("01-120");//git
        st.add("05-123");//git
        st.add("00-000");//git

        for(int i=0; i<st.size(); i++){
            System.out.println(st.get(i)); //powinno wypisac dodane kody pocztowe
        }

        st.remove(0);  //usuwa "02-495"
        st.remove("00-000"); // usuwa "00-000"

        System.out.println("po usunieciu");
        for(int i=0; i<st.size(); i++){
            System.out.println(st.get(i)); //powinno wypisac dodane kody pocztowe
        }

        st.getDataBetween(LocalDateTime.of(2023,12,21,11,11,11),
                LocalDateTime.of(2023,12,23,11,11,11));

        st.storeToFile("postalCodes.txt"); // powinno zapisac zawartosc
        st.fromFile("postalCodes.txt"); // powinno wczytac zawartosc z pliku i "fromFile" musi miec te same dane co "st"

    }
}