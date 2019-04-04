package Ranit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Datasource {
    public static final String DB_NAME = "trainInfo.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:E:\\Ranit\\Java programming\\Databases\\" + DB_NAME;
    public static final String TABLE_TRAINS = "Trains";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "Train_name";
    public static final String TABLE_TRAIN_SCHEDULE = "Train_schedule";
    public static final String COLUMN_TRAINSCHEDULE_TRAINID = "Train_id";
    public static final String COLUMN_TRAINSCHEDULE_SCHEDULEID = "Schedule_id";
    public static final String TABLE_STATION_SCHEDULE = "Station_Schedule";
    public static final String COLUMN_STATIONSCHEDULE_ID = "id";
    public static final String COLUMN_STATIONSCHEDULE_ARRIVALTIME = "Arrival_time";
    public static final String COLUMN_STATIONSCHEDULE_DEPARTURETIME = "Departure_time";
    public static final String COLUMN_STATIONSCHEDULE_STATIONID = "Station_id";
    public static final String TABLE_PLATFORM = "Platform";
    public static final String COLUMN_PLATFORM_ID = "id";
    public static final String COLUMN_PLATFORM_STATUS = "Status";

    //CREATE VIEW `First` AS SELECT Train_schedule.Schedule_id, Trains.Train_name, Station_Schedule.Arrival_time, Station_Schedule.Departure_time, Platform.id from Trains INNER JOIN Train_schedule ON Trains.id=Train_schedule.Train_id INNER JOIN Station_Schedule ON Train_schedule.Schedule_id=Station_Schedule.id LEFT JOIN Platform on Station_Schedule.Station_id=Platform.id
    public static final String PLATFORM_TABLE = "Platform_datasheet";
    public static final String CREATE_PLATFORM_TABLE = "CREATE TABLE " +
            PLATFORM_TABLE + " AS SELECT " + TABLE_TRAIN_SCHEDULE + "." + COLUMN_TRAINSCHEDULE_SCHEDULEID + ", " +
            TABLE_TRAINS + "." + COLUMN_NAME + ", " +
            TABLE_STATION_SCHEDULE + "." + COLUMN_STATIONSCHEDULE_STATIONID + ", " +
            TABLE_STATION_SCHEDULE + "." + COLUMN_STATIONSCHEDULE_ARRIVALTIME + ", " +
            TABLE_STATION_SCHEDULE + "." + COLUMN_STATIONSCHEDULE_DEPARTURETIME + ", " +
            TABLE_PLATFORM + "." + COLUMN_PLATFORM_ID +
            " FROM " + TABLE_TRAINS +
            " INNER JOIN " + TABLE_TRAIN_SCHEDULE + " ON " + TABLE_TRAINS + "." + COLUMN_ID + "=" + TABLE_TRAIN_SCHEDULE + "." + COLUMN_TRAINSCHEDULE_TRAINID +
            " INNER JOIN " + TABLE_STATION_SCHEDULE + " ON " + TABLE_TRAIN_SCHEDULE + "." + COLUMN_TRAINSCHEDULE_SCHEDULEID + "=" + TABLE_STATION_SCHEDULE + "." + COLUMN_STATIONSCHEDULE_ID +
            " LEFT JOIN " + TABLE_PLATFORM + " ON " + TABLE_STATION_SCHEDULE + "." + COLUMN_STATIONSCHEDULE_STATIONID + "=" + TABLE_PLATFORM + "." + COLUMN_PLATFORM_ID;

    public static final String UPDATE_PLATFORMS = "UPDATE " + TABLE_PLATFORM + " SET " + COLUMN_PLATFORM_STATUS + " = \"";
    public static final String UPDATE_PLATFORMS_PART_A = "UPDATE " + PLATFORM_TABLE + " SET " + COLUMN_PLATFORM_ID + " = \"";
    public static final String UPDATE_PLATFORMS_PART_B = "\" WHERE " + COLUMN_TRAINSCHEDULE_SCHEDULEID + " = \"";

    public static final String IF_STATEMENT = "SELECT " + COLUMN_TRAINSCHEDULE_SCHEDULEID + " FROM " + PLATFORM_TABLE + " WHERE " + COLUMN_PLATFORM_ID + " = \"";

    public static final String QUERY_UPDATE_DATASHEET = "UPDATE " + PLATFORM_TABLE + " SET " + COLUMN_PLATFORM_ID + " = ? WHERE " + COLUMN_STATIONSCHEDULE_STATIONID + " = ?";

    public static final String QUERY1 = "SELECT " + COLUMN_STATIONSCHEDULE_DEPARTURETIME + " FROM " + PLATFORM_TABLE + " WHERE " + COLUMN_TRAINSCHEDULE_SCHEDULEID + " = \"";
    public static final String QUERY2 = "SELECT " + COLUMN_STATIONSCHEDULE_ARRIVALTIME + " FROM " + PLATFORM_TABLE + " WHERE " + COLUMN_TRAINSCHEDULE_SCHEDULEID + " = \"";

    public static final int NEXT_TRAIN = 5;
    //private PreparedStatement queryUpdateDatasheet;

    private Connection conn;

    private static Datasource instance = new Datasource();

    private Datasource() {

    }

    public static Datasource getInstance() {
        return instance;
        //Datasource.getInstance().methodName()
    }

    public boolean open() {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);
            //queryUpdateDatasheet = conn.prepareStatement(QUERY_UPDATE_DATASHEET);
            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't establish connection " + e.getMessage());
            return false;
        }
    }

    public void close() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Couldn't close database " + e.getMessage());
        }
    }

    public void updatePlatformtable(String status) {

        StringBuilder sb = new StringBuilder(UPDATE_PLATFORMS);
        sb.append(status);
        sb.append("\"");

        try (Statement statement = conn.createStatement()) {
            statement.execute(sb.toString());
        } catch (SQLException e) {
            System.out.println("Updating platform status failed: " + e.getMessage());
        }
    }

    public String createTableForPlatform() {
        String a;
        try (Statement statement = conn.createStatement()) {
            statement.execute(CREATE_PLATFORM_TABLE);
            a = "Platform table created";
            System.out.println(a);
            updatePlatformtable("Occupied");
            return a;

        } catch (SQLException e) {
            a = "Platform table not created: " + e.getMessage();
            System.out.println(a);
            return a;

        }
    }

    public List<DataCheck2> returnDeparture(int train_No) {
        StringBuilder sb = new StringBuilder(QUERY1);
        sb.append(train_No);
        sb.append("\"");

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sb.toString())) {
            List<DataCheck2> dataChecks = new ArrayList<>();
            while (results.next()) {
                DataCheck2 dataCheck = new DataCheck2();
                dataCheck.setId(results.getDouble(COLUMN_STATIONSCHEDULE_DEPARTURETIME));
                dataChecks.add(dataCheck);
            }
            return dataChecks;
        } catch (SQLException e) {
            System.out.println("Departure not returned " + e.getMessage());
            return null;
        }

    }

    public List<DataCheck2> returnArrival(int train_No) {
        StringBuilder sb = new StringBuilder(QUERY2);
        sb.append(train_No);
        sb.append("\"");

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sb.toString())) {
            List<DataCheck2> dataChecks = new ArrayList<>();
            while (results.next()) {
                DataCheck2 dataCheck = new DataCheck2();
                dataCheck.setId(results.getDouble(COLUMN_STATIONSCHEDULE_ARRIVALTIME));
                dataChecks.add(dataCheck);
            }
            return dataChecks;
        } catch (SQLException e) {
            System.out.println("Arrival not returned " + e.getMessage());
            return null;
        }

    }

    public boolean checkTime(int train_no, int nextTrainId) {
        List<DataCheck2> dataChecks = returnDeparture(train_no);
        List<DataCheck2> dataChecks2 = returnArrival(nextTrainId);
        double dep = -1, ar = -1;
        if (dataChecks == null || dataChecks2 == null) {
            System.out.println("Return value of time failed");
            return false;
        } else {

            for (DataCheck2 dataCheck1 : dataChecks) {
                dep = dataCheck1.getId();
            }
            for (DataCheck2 dataCheck2 : dataChecks2) {
                ar = dataCheck2.getId();
            }
            if (ar > dep) {
                return true;
            } else
                return false;
        }
    }

    public boolean updateNextTrainData(int set, int where) {
        StringBuilder sb = new StringBuilder(UPDATE_PLATFORMS_PART_A);
        sb.append(set);
        sb.append(UPDATE_PLATFORMS_PART_B);
        sb.append(where);
        sb.append("\"");

        System.out.println(sb.toString());

        try (Statement statement = conn.createStatement()) {
            statement.execute(sb.toString());
            return true;
        } catch (SQLException e) {
            System.out.println("Update process failed:" + e.getMessage());
            return false;
        }


    }

    /*public void updatePlaformDatasheet (int set, int where){
        try {
            queryUpdateDatasheet.setInt(1, set);
            queryUpdateDatasheet.setInt(2, where);
        }
        catch(SQLException e){
            System.out.println("Update failed: "+e.getMessage());
        }
    }*/

    public List<DataCheck> checkForEachStationID(int data) {
        //Checking for paltform no
        StringBuilder sb = new StringBuilder(IF_STATEMENT);
        sb.append(data);
        sb.append("\"");

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sb.toString())) {

            List<DataCheck> dataChecks = new ArrayList<>();
            while (results.next()) {
                DataCheck dataCheck = new DataCheck();
                dataCheck.setId(results.getInt(COLUMN_TRAINSCHEDULE_SCHEDULEID));
                dataChecks.add(dataCheck);
            }
            return dataChecks;
        } catch (SQLException e) {
            System.out.println("Checking Failed at stage " + data);
            return null;
        }
    }

    public String checkIfStatement(int platformData, int nextTrainId) {
        String stat = "Retry";
        outerLoop:
        for (platformData = 1; platformData <= 4; platformData++) {
            List<DataCheck> dataChecks = checkForEachStationID(platformData);

            if (dataChecks == null) {
                stat = "No platform assigned to train";
                System.out.println(stat);
                //return stat;
            } else {
                for (DataCheck dataCheck : dataChecks) {
                    int train_no = dataCheck.getId();
                    boolean check = checkTime(train_no, nextTrainId);
                    if (check) {
                        updateNextTrainData(platformData, nextTrainId);
                        updateNextTrainData(-1, train_no);
                        stat = "Train " + nextTrainId + " has been assigned to platform " + platformData;
                        System.out.println(stat);
                        break outerLoop;
                    } else {
                        stat = "Train " + nextTrainId + " cannot be assigned to platform " + platformData;
                        System.out.println(stat);

                    }

                }

            }
        }
        return stat;
    }
}




