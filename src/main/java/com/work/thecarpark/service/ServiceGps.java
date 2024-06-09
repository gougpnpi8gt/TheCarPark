package com.work.thecarpark.service;

import com.work.thecarpark.entity.gps.Gps;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceGps {
    public double processFile(MultipartFile file) throws IOException {
        List<Gps> gpsList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            Gps previousData = null;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("$GPGGA")) {
                    Gps currentData = parseGpgga(line);
                    if (previousData != null && currentData != null) {
                        gpsList.add(currentData);
                    }
                    previousData = currentData;
                } else if (line.startsWith("$GNVTG")) {
                    double speed = parseGnvgt(line);
                    if (speed == 0 && !gpsList.isEmpty()) {
                        gpsList.removeLast();
                    }
                }
            }
        }
        return calculateTotalDistance(gpsList);
    }

    private Gps parseGpgga(String line) {
        String[] tokens = line.split(",");
        if (tokens.length > 5) {
            double latitude = Double.parseDouble(tokens[2]);
            double longitude = Double.parseDouble(tokens[4]);
            return new Gps(latitude, longitude);
        }
        return null;
    }

    private double parseGnvgt(String line) {
        String[] tokens = line.split(",");
        if (tokens.length > 7) {
            return Double.parseDouble(tokens[7]);
        }
        return 0;
    }

    private double calculateTotalDistance(List<Gps> gpsList) {
        double totalDistance = 0;
        for (int i = 1; i < gpsList.size(); i++) {
            Gps point1 = gpsList.get(i - 1);
            Gps point2 = gpsList.get(i);
            totalDistance += haversine(point1.getLatitude(), point1.getLongitude(), point2.getLatitude(), point2.getLongitude());
        }
        return totalDistance;
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // convert to kilometers
    }
}
