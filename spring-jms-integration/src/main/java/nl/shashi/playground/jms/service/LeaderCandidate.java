package nl.shashi.playground.jms.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.leader.Context;
import org.springframework.integration.leader.DefaultCandidate;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

@Component
@Slf4j
public class LeaderCandidate extends DefaultCandidate {

    private final AtomicBoolean leader = new AtomicBoolean(false);

    @Override
    public void onGranted(Context ctx) {
        super.onGranted(ctx);
        leader.set(true);
        log.info("*********************** Became leader ***************************");
    }

    @Override
    public void onRevoked(Context ctx) {
        super.onRevoked(ctx);
        leader.set(false);
        log.info("*********************** Lost leadership ***************************");
    }

    public boolean isLeader() {
        return leader.get();
    }
}
