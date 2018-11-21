package src.main.resources.script

/*
┌This Lightning was jarred in Portland, OR by Trace Andreason┐
│[===][===][===][===][===][===][===][===][===][===][===][===]│
││ ϟ ││ ϟ ││ ϟ ││ ϟ ││ ϟ ││ ϟ ││ ϟ ││ ϟ ││ ϟ ││ ϟ ││ ϟ ││ ϟ ││
│└───┘└───┘└───┘└───┘└───┘└───┘└───┘└───┘└───┘└───┘└───┘└───┘│
└────────────────────────────────────────────────────────────┘
 */

import org.apache.camel.Exchange;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.SimpleBuilder;

class SCPIGW {
    def message
    def messageBody
    def exchange
    def camelContext
    def messageHeaderKeys
    def messagePropertyKeys
    def exchangeVariableKeys
    def persistentVariableKeys
    def externalizedParameterReads
    def history

    SCPIGW(message) {
        this.message = message
        this.exchange = message.exchange
        this.camelContext = this.exchange.getContext()
        if (this.message.getBody().getClass() == java.lang.String) {
            this.messageBody = message.getBody()
        } else {
            this.messageBody = message.getBody(java.lang.String)
        }
        this.messageHeaderKeys = []
        this.messagePropertyKeys = []
        this.exchangeVariableKeys = []
        this.persistentVariableKeys = []
        this.externalizedParameterReads = []
        this.history = []
    }

    def getMessageBody() {
        return this.messageBody
    }
    
    def saveBody() {
        this.message.setBody(this.messageBody);
    }

    def preserve() {
        this.saveBody()
        this.history.add(this.dumpState())
        this.setExchangeVariable("gw", this)
    }
    
    def evaluateSimple(simpleExpression) {
        SimpleBuilder.simple(simpleExpression).evaluate(this.exchange, String)
    }
    
    def getExternalizedParameter(name) {
    // Gets an externalized paramter
        try {
            def tmp = evaluateSimple("{{" + name + "}}")
            if (!this.externalizedParameterReads.contains(name)) {
                this.externalizedParameterReads.add(name);
            }
            return tmp;
        } catch (IllegalArgumentException) {
            return null;
        }
    }

    def getPersistentVariable(name) {
        try {
            return this.camelContext.getProperty(name);
        } catch (MissingPropertyException) {
            return null;
        }
    }

    def setPersistentVariable(key, value) {
        if (!this.persistentVariableKeys.contains(key)) {
            this.persistentVariableKeys.add(key);
        }
        def tmp = this.camelContext.getProperties();
        tmp.put(key,value);
        this.camelContext.setProperties(tmp);
    }

    def getExchangeVariable(name) {
        try {
            return this.exchange.getProperty(name);
        } catch (MissingPropertyException) {
            return null;
        }
    }
    
    def setExchangeVariable(key, value) {
        if (!this.exchangeVariableKeys.contains(key)) {
            this.exchangeVariableKeys.add(key);
        }
        this.exchange.setProperty(key, value);
    }

    def getMessageHeader(name) {
        //return this.message.getHeader(name, java.lang.String)
        try {
            return this.message.getHeaders().get(name)
        } catch (MissingPropertyException) {
            return null;
        }
    }

    def setMessageHeader(key, value) {
        if (!this.messageHeaderKeys.contains(key)) {
            this.messageHeaderKeys.add(key);
        }
        this.message.setHeader(key, value)
    }

    def getMessageProperty(name) {
        try {
            return this.message.getProperty(name)
        } catch (MissingPropertyException) {
            return null;
        }
    }

    def setMessageProperty(key, value) {
        if (!this.messagePropertyKeys.contains(key)) {
            this.messagePropertyKeys.add(key);
        }
        this.message.setProperty(key, value);
    }
    
    def dumpState() {
        this.saveBody();
        StringBuilder sb = new StringBuilder();

        sb << "-- Accessed External Variables --" + '\r\n'
        this.externalizedParameterReads.each { key ->
            sb << key + ": " + this.getExternalizedParameter(key) + ", ";
        }
        sb << "\r\n"

        sb << "-- Persistent Variables --" + '\r\n'
        sb << this.camelContext.getProperties().toString() + '\r\n'
        
        sb << "-- Exchange Variables --" + '\r\n'
        this.exchangeVariableKeys.each { key ->
            sb << key + ": " + this.getExchangeVariable(key) + ", ";
        }
        sb << "\r\n"
        //sb << this.exchange.getProperties().toString() + '\r\n'
        sb << "-- message properties --" + '\r\n'
        
        this.messagePropertyKeys.each { key ->
            sb << key + ": " + this.getMessageProperty(key) + ", ";
        }
        sb << "\r\n"
	//sb << this.message.getProperties().toString() + '\r\n'
        
        sb << "-- message headers --" + '\r\n'
        this.messageHeaderKeys.each { key ->
            sb << key + ": " + this.getMessageHeader(key) + ", ";
        }
        sb << "\r\n"
	//sb << this.message.getHeaders().toString() + '\r\n'
        
	sb << "-- message body --" + '\r\n'
	sb << this.messageBody + '\r\n'
	return sb.toString()
    }

    def addHistoryPoint() {
        this.history.add(this.dumpState());
    }

    def dumpHistory() {
        StringBuilder sb = new StringBuilder();
        sb << "MESSAGE HISTORY" + '\r\n';
        this.history.eachWithIndex { historyPoint, i ->
            sb << "--- History Point " + i + " ---" + '\r\n';
            sb << historyPoint;
        }
        return sb.toString()
    }
}
