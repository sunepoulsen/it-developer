package dk.sunepoulsen.itdeveloper.ui.tasks.backend;

import dk.sunepoulsen.itdeveloper.backend.BackendConnection;
import dk.sunepoulsen.itdeveloper.services.FlexBalanceService;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
public class CalculateFlexBalanceTask extends BackendConnectionTask<Double> {
    private final LocalDate balanceDate;

    public CalculateFlexBalanceTask(BackendConnection connection, LocalDate balanceDate) {
        super(connection, "calc.balance.task");
        this.balanceDate = balanceDate;
    }

    @Override
    protected Double call() throws Exception {
        try {
            return new FlexBalanceService(connection).flexBalance(balanceDate);
        } catch (Exception ex) {
            log.info("Unable to calculate flex balance for date {} -> {}", balanceDate, ex.getMessage());
            log.debug("Exception", ex);
            failed();
            return 0.0;
        }
    }
}
