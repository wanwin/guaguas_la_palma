package marrero.hamad.darwin.guaguaslapalma.model;

/**
 * Created by Antonio on 10/11/2017.
 */

/* Copyright 2014 Esri

All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/***
 * A class representing a bus route, which includes a list of x and y coordinates,
 * as well as 'slow' and 'stop' flags for when taxis are nearing, or at, intersections.
 *
 */
public class BusRoute {

    private List<Double> xPos = new ArrayList<>();
    private List<Double> yPos =  new ArrayList<>();
    private List<Integer> slowFlag = new ArrayList<>();
    private List<Integer> stopFlag = new ArrayList<>();

    public BusRoute(String routeFile) {
        readRouteFromFile(routeFile);
    }

    public List<Double> getXPos() {
        return xPos;
    }

    public List<Double> getYPos() {
        return yPos;
    }

    public List<Integer> getSlowFlag() {
        return slowFlag;
    }

    public List<Integer> getStopFlag() {
        return stopFlag;
    }

    // read route into memory
    private void readRouteFromFile(String routeFile)
    {

        File file = new File(routeFile);

        BufferedReader bufRdr = null;
        String line;
        int row = 0;
        int col = 0;
        String csvItem;
        try {
            bufRdr = new BufferedReader(new FileReader(file));
            while((line = bufRdr.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line,",");

                while (st.hasMoreTokens()) {

                    csvItem = st.nextToken();

                    switch (col) {
                        case 0:
                            //position ID
                            break;
                        case 1:
                            //route ID
                            break;
                        case 2:
                            //X pos
                            xPos.add(row, Double.valueOf(csvItem));
                            break;
                        case 3:
                            //Y pos
                            yPos.add(row, Double.valueOf(csvItem));
                            break;
                        case 4:
                            //slow down flag
                            slowFlag.add(row, Integer.valueOf(csvItem));
                            break;
                        case 5:
                            //stop at junction flag
                            stopFlag.add(row, Integer.valueOf(csvItem));
                            break;
                    }
                    col++;
                }
                col = 0;
                row++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufRdr != null) {
                try {
                    bufRdr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}