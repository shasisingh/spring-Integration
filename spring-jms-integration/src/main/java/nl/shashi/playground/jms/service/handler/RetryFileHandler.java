package nl.shashi.playground.jms.service.handler;

import nl.shashi.playground.jms.service.LeaderCandidate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
public class RetryFileHandler implements MessageHandler {

    private final LeaderCandidate leaderCandidate;
    private final DataSource dataSource;

    public RetryFileHandler(LeaderCandidate leaderCandidate, DataSource dataSource) {
        this.leaderCandidate = leaderCandidate;
        this.dataSource = dataSource;
    }

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        if (!leaderCandidate.isLeader()) {
            throw new MessagingException("Cannot handle the message because the service is not the leader");
        }
        List<Map<String, String>> retryMessagePayload = (List<Map<String, String>>) message.getPayload();

        for (Map<String, String> payload : retryMessagePayload) {
            String sourceDirectory = payload.get("SOURCE_DIR");
            String fileScanUUID = payload.get("FILE_UUID");
            System.out.println(fileScanUUID + sourceDirectory);
            retryMetaDataCleanup(fileScanUUID);
            System.out.println("Consumed Jdbc Message");
        }
    }

    private void retryMetaDataCleanup(final String fileScanUUID) {
        String sqlStatement = "DELETE FROM INT_RETRY_METADATA WHERE FILE_UUID=?";
        try (Connection connection = dataSource.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement)) {
            preparedStatement.setString(1, fileScanUUID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("retryFileHandler:retry: Error while deleting to database");
        }
    }

}
