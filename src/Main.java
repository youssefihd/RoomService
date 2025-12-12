import enums.RoomType;
import service.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    public static void main(String[] args) throws ParseException {
        Service service = new Service();

        // 1) Create 3 rooms
        // Room 1: ID 1, Type standard, Price/night 1000
        service.setRoom(1, RoomType.standard, 1000);

        // Room 2: ID 2, Type junior, Price/night 2000
        service.setRoom(2, RoomType.junior, 2000);

        // Room 3: ID 3, Type suite, Price/night 3000
        // (if your enum is MASTER_SUITE or SUITE, use that value)
        service.setRoom(3, RoomType.suite, 3000);

        // 2) Create 2 users (IDs 1 and 2 with balances 5000 and 10000)
        // I added first/last names because your Service.setUser needs them
        service.setUser(1, "User", "One", 5000);
        service.setUser(2, "User", "Two", 10000);

        // 3) User 1 tries booking Room 2 from 30/06/2026 to 07/07/2026 (7 nights)
        Date d1_checkIn  = DATE_FORMAT.parse("30/06/2026");
        Date d1_checkOut = DATE_FORMAT.parse("07/07/2026");
        service.bookRoom(1, 2, d1_checkIn, d1_checkOut);

        // 4) User 1 tries booking Room 2 from 07/07/2026 to 30/06/2026 (invalid, check-out before check-in)
        Date d2_checkIn  = DATE_FORMAT.parse("07/07/2026");
        Date d2_checkOut = DATE_FORMAT.parse("30/06/2026");
        service.bookRoom(1, 2, d2_checkIn, d2_checkOut);

        // 5) User 1 tries booking Room 1 from 07/07/2026 to 08/07/2026 (1 night)
        Date d3_checkIn  = DATE_FORMAT.parse("07/07/2026");
        Date d3_checkOut = DATE_FORMAT.parse("08/07/2026");
        service.bookRoom(1, 1, d3_checkIn, d3_checkOut);

        // 6) User 2 tries booking Room 1 from 07/07/2026 to 09/07/2026 (2 nights)
        Date d4_checkIn  = DATE_FORMAT.parse("07/07/2026");
        Date d4_checkOut = DATE_FORMAT.parse("09/07/2026");
        service.bookRoom(2, 1, d4_checkIn, d4_checkOut);

        // 7) User 2 tries booking Room 3 from 07/07/2026 to 08/07/2026 (1 night)
        Date d5_checkIn  = DATE_FORMAT.parse("07/07/2026");
        Date d5_checkOut = DATE_FORMAT.parse("08/07/2026");
        service.bookRoom(2, 3, d5_checkIn, d5_checkOut);

        service.setRoom(1, RoomType.suite, 10000);

        // 9) Print final state
        service.printAll();
        service.printAllUsers();
    }
    }
