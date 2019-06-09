package ua.hospes.rtm.utils;

import android.os.Environment;
import android.util.SparseIntArray;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import jxl.CellView;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import ua.hospes.rtm.domain.cars.Car;
import ua.hospes.rtm.domain.drivers.Driver;
import ua.hospes.rtm.domain.sessions.Session;
import ua.hospes.rtm.domain.team.Team;

/**
 * @author Andrew Khloponin
 */
public class XLSTest {
    private static final String DIR_NAME_MAIN = "ua.hospes.rtm";
    private static final String DIR_NAME_XLS = "xls";
    private static final int COLUMNS_PER_DRIVER = 2;
    private static final CellView cvCarSize = new CellView();
    private static final CellView cvTimeSize = new CellView();

    static {
        cvCarSize.setSize(4 * 256);
        cvTimeSize.setSize(13 * 256);
    }

    public static File createWorkbook(Map<Team, List<Session>> data) throws IOException, WriteException {
        File dir = new File(Environment.getExternalStorageDirectory(), DIR_NAME_MAIN + File.separator + DIR_NAME_XLS);
        if (!dir.exists()) dir.mkdirs();
        File result = new File(dir, "output.xls");

        WritableWorkbook book = Workbook.createWorkbook(result);
        try {
            WritableSheet sheet = book.createSheet("Need For Speed", 0);
            int teamColumn = 0;
            for (Team team : data.keySet()) {
                fillTeamHeader(sheet, teamColumn, team);
                SparseIntArray driverIds = new SparseIntArray();
                for (int i = 0; i < team.getDrivers().size(); i++) driverIds.put(i, team.getDrivers().get(i).getId());
                fillTeamSessions(sheet, teamColumn, driverIds, data.get(team));
                teamColumn += team.getDrivers().size() * COLUMNS_PER_DRIVER + 1;
            }
            book.write();
        } finally {
            book.close();
        }
        return result;
    }

    private static void fillTeamHeader(WritableSheet sheet, int column, Team team) throws WriteException {
        sheet.addCell(new Label(column, 0, team.getName()));
        sheet.mergeCells(column, 0, column + team.getDrivers().size() * COLUMNS_PER_DRIVER, 0);
        sheet.addCell(new Label(column, 2, "Car"));
        for (int i = 0; i < team.getDrivers().size(); i++) {
            Driver driver = team.getDrivers().get(i);
            int driverColumn = column + 1 + i * COLUMNS_PER_DRIVER;
            sheet.addCell(new Label(driverColumn, 1, driver.getName()));
            sheet.mergeCells(driverColumn, 1, driverColumn + 1, 1);
            sheet.addCell(new Label(driverColumn, 2, "Start time"));
            sheet.addCell(new Label(driverColumn + 1, 2, "Duration"));
        }
    }

    private static void fillTeamSessions(WritableSheet sheet, int column, SparseIntArray driverIds, Iterable<Session> sessions) throws WriteException {
        int row = 3;
        for (Session session : sessions) {
            fillTeamSession(sheet, column, row, driverIds, session);
            row++;
        }
    }

    private static void fillTeamSession(WritableSheet sheet, int column, int row, SparseIntArray driverIds, Session session) throws WriteException {
        Driver driver = session.getDriver();
        int i = driver == null ? 0 : driverIds.indexOfValue(driver.getId());

        // Write Car
        Car car = session.getCar();
        sheet.addCell(new Label(column, row, car == null ? "" : String.valueOf(car.getNumber())));
        sheet.setColumnView(column, cvCarSize);

        // Write start time
        sheet.addCell(new Label(column + 1 + i * COLUMNS_PER_DRIVER, row, TimeUtils.formatNanoWithMills(session.getStartDurationTime() - session.getRaceStartTime())));
        sheet.setColumnView(column + 1 + i * COLUMNS_PER_DRIVER, cvTimeSize);

        // Write duration
        sheet.addCell(new Label(column + 1 + i * COLUMNS_PER_DRIVER + 1, row, TimeUtils.formatNanoWithMills(session.getEndDurationTime() - session.getStartDurationTime())));
        sheet.setColumnView(column + 1 + i * COLUMNS_PER_DRIVER + 1, cvTimeSize);
    }
}