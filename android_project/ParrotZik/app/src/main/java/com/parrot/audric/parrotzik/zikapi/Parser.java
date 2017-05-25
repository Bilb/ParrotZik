package com.parrot.audric.parrotzik.zikapi;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by audric on 21/05/17.
 */

public class Parser {

    private static final String TAG = "Parser";

    private final State state;

    public Parser(State state) {
        this.state = state;
    }

    public void parse(InputStream xml) {
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(xml, null);
            handleEvent(parser);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    public void parse(String s) {
        parse(new ByteArrayInputStream(s.getBytes()));
    }


    public void handleEvent(XmlPullParser parser) {
        int event = 0;
        try {
            event = parser.getEventType();
            switch (event) {
                case XmlPullParser.START_TAG:
                    parseTag(parser);
                default:
                    break;
            }

            if(event != XmlPullParser.END_DOCUMENT) {
                parser.next();
                handleEvent(parser);
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    private void parseTag(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(parser.getName().toLowerCase().equals("answer")) {
            parseAnswer(parser);
            Log.i(TAG, "after parse : state: " +state);
        }
    }


    private void parseAnswer(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.nextTag();
        if (parser.getName().toLowerCase().equals("audio"))
            parseAudio(parser);
        else if (parser.getName().toLowerCase().equals("system")) {
            parseSystem(parser);
        }

        parser.nextTag();
    }

    private void parseAudio(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.nextTag();
        if (parser.getName().toLowerCase().equals("noise_cancellation"))
            parseNoiseCancellation(parser);
        else if (parser.getName().toLowerCase().equals("equalizer"))
            parseEqualizer(parser);
        else if (parser.getName().toLowerCase().equals("sound_effect"))
            parseConcertHall(parser);

        parser.nextTag();
    }


    private void parseSystem(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.nextTag();
        if (parser.getName().toLowerCase().equals("battery"))
            parseBattery(parser);

        parser.nextTag();
    }


    private void parseBattery(XmlPullParser parser) throws IOException, XmlPullParserException {
        String batteryStateString = new String(parser.getAttributeValue(null, "state"));
        State.Battery.BatteryState batteryState = State.Battery.BatteryState.UNKNOW;
        if(batteryStateString != null) {
            batteryStateString = batteryStateString.toLowerCase();
            if (batteryStateString.equals("charging"))
                batteryState = State.Battery.BatteryState.CHARGING;
            else if (batteryStateString.equals("in_use")) {
                batteryState = State.Battery.BatteryState.IN_USE;
            }
        }

        state.getBattery().state = batteryState;
        try {
            state.getBattery().level = new Integer(parser.getAttributeValue(null, "level"));
        }
        catch (NumberFormatException e) {
            state.getBattery().level = -1;
        }
    }


    private void parseNoiseCancellation(XmlPullParser parser)
            throws IOException, XmlPullParserException {
        state.setNoiseCancellation(Boolean.parseBoolean(parser.getAttributeValue(null, "enabled")));
    }

    private void parseEqualizer(XmlPullParser parser)
            throws IOException, XmlPullParserException {
        state.setEqualizer(Boolean.parseBoolean(parser.getAttributeValue(null, "enabled")));
    }

    private void parseConcertHall(XmlPullParser parser)
            throws IOException, XmlPullParserException {
        state.setConcertHall(Boolean.parseBoolean(parser.getAttributeValue(null, "enabled")));
    }

}
