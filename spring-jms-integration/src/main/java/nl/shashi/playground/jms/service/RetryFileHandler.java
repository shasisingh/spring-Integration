package nl.shashi.playground.jms.service;


import org.springframework.integration.leader.Context;
import org.springframework.integration.leader.DefaultCandidate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class RetryFileHandler extends DefaultCandidate implements MessageHandler {

    private final DataSource dataSource;

    public RetryFileHandler( DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {

        List<Map<String, String>> retryMessagePayload = (List<Map<String, String>>) message.getPayload();

        for (Map<String, String> payload : retryMessagePayload) {
            List<String> retryFilesName = new ArrayList<>();
            String sourceDirectory = payload.get("SOURCE_DIR");
            String fileScanUUID = payload.get("FILE_UUID");
            System.out.println(fileScanUUID+ sourceDirectory);
            retryMetaDataCleanup(fileScanUUID);
            System.out.println("Consumed Jdbc Message");
        }
    }

    private void retryMetaDataCleanup(final String fileScanUUID) {
        String sqlStatement = "DELETE FROM INT_RETRY_METADATA WHERE FILE_UUID=?";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement)) {
            preparedStatement.setString(1, fileScanUUID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("retryFileHandler:retry: Error while deleting to database");
        }
    }

    @Override
    public void onGranted(Context ctx) {
        super.onGranted(ctx);
        System.out.println("*********************** Became leader *************************** ");
    }

    @Override
    public void onRevoked(Context ctx) {
        super.onRevoked(ctx);
        System.out.println("************************ Lost leadership ************************* ");
    }


}
