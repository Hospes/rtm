package ua.hospes.rtm.utils

import android.os.Environment
import android.util.SparseIntArray
import jxl.CellView
import jxl.Workbook
import jxl.write.Label
import jxl.write.WritableSheet
import jxl.write.WriteException
import ua.hospes.rtm.domain.sessions.Session
import ua.hospes.rtm.domain.team.Team
import java.io.File
import java.io.IOException

/**
 * @author Andrew Khloponin
 */
object XLSTest {
    private val DIR_NAME_MAIN = "ua.hospes.rtm"
    private val DIR_NAME_XLS = "xls"
    private val COLUMNS_PER_DRIVER = 2
    private val cvCarSize = CellView()
    private val cvTimeSize = CellView()

    init {
        cvCarSize.size = 4 * 256
        cvTimeSize.size = 13 * 256
    }

    @Throws(IOException::class, WriteException::class)
    fun createWorkbook(data: Map<Team, List<Session>>): File {
        val dir = File(Environment.getExternalStorageDirectory(), DIR_NAME_MAIN + File.separator + DIR_NAME_XLS)
        if (!dir.exists()) dir.mkdirs()
        val result = File(dir, "output.xls")

        val book = Workbook.createWorkbook(result)
        try {
            val sheet = book.createSheet("Need For Speed", 0)
            var teamColumn = 0
            for (team in data.keys) {
                fillTeamHeader(sheet, teamColumn, team)
                val driverIds = SparseIntArray()
                for (i in 0 until team.drivers.size) driverIds.put(i, team.drivers[i].id!!)
                fillTeamSessions(sheet, teamColumn, driverIds, data[team]!!)
                teamColumn += team.drivers.size * COLUMNS_PER_DRIVER + 1
            }
            book.write()
        } finally {
            book.close()
        }
        return result
    }

    @Throws(WriteException::class)
    private fun fillTeamHeader(sheet: WritableSheet, column: Int, team: Team) {
        sheet.addCell(Label(column, 0, team.name))
        sheet.mergeCells(column, 0, column + team.drivers.size * COLUMNS_PER_DRIVER, 0)
        sheet.addCell(Label(column, 2, "Car"))
        for (i in 0 until team.drivers.size) {
            val (_, name) = team.drivers[i]
            val driverColumn = column + 1 + i * COLUMNS_PER_DRIVER
            sheet.addCell(Label(driverColumn, 1, name))
            sheet.mergeCells(driverColumn, 1, driverColumn + 1, 1)
            sheet.addCell(Label(driverColumn, 2, "Start time"))
            sheet.addCell(Label(driverColumn + 1, 2, "Duration"))
        }
    }

    @Throws(WriteException::class)
    private fun fillTeamSessions(sheet: WritableSheet, column: Int, driverIds: SparseIntArray, sessions: Iterable<Session>) {
        var row = 3
        for (session in sessions) {
            fillTeamSession(sheet, column, row, driverIds, session)
            row++
        }
    }

    @Throws(WriteException::class)
    private fun fillTeamSession(sheet: WritableSheet, column: Int, row: Int, driverIds: SparseIntArray, session: Session) {
        val driver = session.driver
        val i = if (driver == null) 0 else driverIds.indexOfValue(driver.id!!)

        // Write Car
        val car = session.car
        sheet.addCell(Label(column, row, car?.number?.toString() ?: ""))
        sheet.setColumnView(column, cvCarSize)

        // Write start time
        sheet.addCell(Label(column + 1 + i * COLUMNS_PER_DRIVER, row, TimeUtils.formatNanoWithMills(session.startDurationTime - session.raceStartTime)))
        sheet.setColumnView(column + 1 + i * COLUMNS_PER_DRIVER, cvTimeSize)

        // Write duration
        sheet.addCell(Label(column + 1 + i * COLUMNS_PER_DRIVER + 1, row,
                TimeUtils.formatNanoWithMills(session.endDurationTime ?: 0L - session.startDurationTime)))
        sheet.setColumnView(column + 1 + i * COLUMNS_PER_DRIVER + 1, cvTimeSize)
    }
}