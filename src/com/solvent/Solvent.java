package com.solvent;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.solvent.l10nHelper.I18NUtil;

/**
 * Created by reed on 16/10/13.
 */
public abstract class Solvent {
    private static final Logger log = SolventLogger.getLogger(Solvent.class);
    protected final HashMap<String, String> parameters = new HashMap<String, String>();

    public Solvent() {
        I18NUtil.processI18NKeys(this);
    }

    public void parseParams(String... params) {
        for (String param : params) {
            try {
                String name = param.substring(0, param.indexOf('=')).trim();
                String value = param.substring(param.indexOf('=') + 1).trim();

                if (!parameters.containsKey(name)) {
                    parameters.put(name, value);
                } else {
                    log.warn("Duplicated parameter '" + name + "' given. Ignoring...");
                }

            } catch (Exception e) {
                log.error("Error - " + e.getMessage());
            }
        }
    }

    public String getParam(String name) {
        if (!parameters.containsKey(name)) {
            log.debug("Requested parameter '" + name + "' does not exist! Returning null");
        }
        return parameters.get(name);
    }

    public void setParam(String name, String value) {
        parameters.put(name, value);

    }

    protected boolean paramDefined(String paramName) {
        return parameters.containsKey(paramName)
                && (!parameters.get(paramName).equals("") && parameters.get(paramName) != null);
    }

    public abstract void prepare();

    public <X extends Solvent> X navigateTo(X sol) {
        sol.prepare();
        return sol;
    }

    public static void waitFor(long time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            log.error("\n\n Time Out With Exception: " + e.getMessage());
        }
    }
}
