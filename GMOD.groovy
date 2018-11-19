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

class GMOD{
    def message
    def exchange
    def camelContext
    def messageBody

    GMOD(message) {
        this.message = message
        this.exchange = message.exchange
        this.camelContext = this.exchange.getContext()
        if (this.message.getBody().getClass() == java.lang.String) {
            this.messageBody = message.getBody()
        } else {
            this.messageBody = message.getBody(java.lang.String)
        }
    }

    def getPayloadBody() {
        return this.messageBody
    }
    
    def saveBody() {
        this.message.setBody(this.messageBody);
    }
    
    def evaluateSimple(simpleExpression) {
        SimpleBuilder.simple(simpleExpression).evaluate(this.exchange, String)
    }
    
    def getExternal(name) {
    // Gets an externalized paramter
        try {
            return evaluateSimple("{{" + name + "}}");
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
        this.exchange.setProperty(key, value);
    }

    def getMessageHeader(name) {
        try {
            return this.message.getHeader(name, java.lang.String)
        } catch (MissingPropertyException) {
            return null;
        }
    }

    def setMessageHeader(key, value) {
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
        this.message.setProperty(key, value);
    }
    
    def dumpState(body) {
        this.message.setBody(body);
        StringBuilder sb = new StringBuilder();
        sb << "----- CamelContext -----" + '\r\n'
        sb << "-- Persistent Variables --" + '\r\n'
        sb << this.camelContext.getProperties().toString() + '\r\n'
        
        sb << "----- Exchange -----" + '\r\n'
        sb << "-- Exchange Variables --" + '\r\n'
        sb << this.exchange.getProperties().toString() + '\r\n'
        
        sb << "----- MESSAGE CLASS -----" + '\r\n'
        sb << "-- message properties --" + '\r\n'
	    sb << this.message.getProperties().toString() + '\r\n'
        sb << "-- message headers --" + '\r\n'
	    sb << this.message.getHeaders().toString() + '\r\n'
	    sb << "-- message body --" + '\r\n'
	    sb << this.messageBody + '\r\n'
	    return sb.toString()
    }
}
