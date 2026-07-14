package net.cero.ahorro.common;

import net.cero.spring.dao.excepcion.DaoException;
import org.springframework.validation.BindingResult;

import javax.sql.DataSource;

public abstract class ConnectionProvider {
    protected DataSource driverSourceCero;
    protected DataSource driverSourceProcrea;
    protected DataSource driverSourceIzel;

    public void init() {
        driverSourceCero = DbBeans.DbBeansImpl.getDataSource("ds");
        driverSourceProcrea = DbBeans.DbBeansImpl.getDataSource("dsPr");
        driverSourceIzel = DbBeans.DbBeansImpl.getDataSource("dsSti");
    }

    public abstract void validationAndAuthentication(Object body, BindingResult bindingResult) throws DaoException;
    public DataSource getDriverSourceCero() {
        return driverSourceCero;
    }

    public void setDriverSourceCero(DataSource driverSourceCero) {
        this.driverSourceCero = driverSourceCero;
    }

    public DataSource getDriverSourceProcrea() {
        return driverSourceProcrea;
    }

    public void setDriverSourceProcrea(DataSource driverSourceProcrea) {
        this.driverSourceProcrea = driverSourceProcrea;
    }

    public DataSource getDriverSourceIzel() {
        return driverSourceIzel;
    }

    public void setDriverSourceIzel(DataSource driverSourceIzel) {
        this.driverSourceIzel = driverSourceIzel;
    }
}
