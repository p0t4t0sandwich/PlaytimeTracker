package ca.sperrer.bungee;

import net.md_5.bungee.api.plugin.Plugin;
import java.util.concurrent.TimeUnit;
import net.md_5.bungee.api.scheduler.ScheduledTask;

public final class PlaytimeTracker extends Plugin {
    @Override
    public void onEnable() {
        getLogger().info("has loaded!");
        ScheduledTask scheduledTask = getProxy().getScheduler().schedule(this, DataSource::update_playtime, 0, 1, TimeUnit.MINUTES);
    }
}
