package com.solvent;

import java.util.HashMap;

/**
 * Created by reed on 16/10/13.
 */
public abstract class Solvent {
    private static final Logger log = SolventLogger.getLogger(Solvent.class);
    protected final HashMap<String, String> parameters = new HashMap<String, String>();

    public Solvent() {
        I19NUtil.processI18NKeys(this);
    }

    public void parseParams(String... params) {
        for (String param:params) {
            try {
                String name = param.substring(0, param.indexOf('=').trim());
                String value = param.substring(param.indexOf('=')+1).trim();

                if (!parameters.containsKey(name)) {
                    parameters.put(name, value);
                } else {
                    log.warm("Duplicated parameter '" + name + "' given. Ignoring...");
                }

            } catch (Exception e) {
                log.error();
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

    public <X extends Solvent> X navigateTo(X se) {
        se.prepare();
        return se;
    }

    public static void waitfor(long milles) {
        try {
            Thread.sleep(milles);
        } catch (Exception e) {
            log.error("\n\n Time Out With Exception: " + e.getMessage());
        }
    }
}
