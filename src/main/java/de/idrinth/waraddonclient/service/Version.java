package de.idrinth.waraddonclient.service;

import de.idrinth.waraddonclient.Utils;
import javax.swing.JLabel;

public class Version implements java.lang.Runnable {
    private final Request client;
    private JLabel label;
    private final FileLogger logger;

    public Version(Request client, FileLogger logger) {
        this.client = client;
        this.logger = logger;
    }

    public void setVersion(JLabel version) {
        this.label = version;
    }

    @Override
    public void run() {
        Utils.sleep(2500, logger);
        try {
            label.setText(client.getVersion());
        } catch (java.lang.Exception exception) {
            logger.error(exception);
        }
    }
}
