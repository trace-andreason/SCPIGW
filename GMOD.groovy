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

    GMOD(message) {
        this.message = message
        this.exchange = message.exchange
        this.camelContext = this.exchange.getContext()
    }

    def getPayloadBody() {
        if (this.message.getBody().getClass() == java.lang.String) {
            return message.getBody()
        }
        return message.getBody(java.lang.String)
    }
    
    def evaluateSimple(simpleExpression) {
        SimpleBuilder.simple(simpleExpression).evaluate(this.exchange,
                                                        String)
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
            this.camelContext.getProperty(name);
        } catch (MissingPropertyException) {
            return null;
        }
    }

    def setPersistentVariable(key, value) {
        def tmp = this.camelContext.getProperties();
        tmp.put(key,value);
        this.camelContext.setProperties(tmp);
    }

    def getExchangeVariable() {
    // Gets the value of an Exchange Variable
        //TODO: Implement
    }
    
    def setExchangeVariable() {
    // Sets the value of an Exchange Variable
        //TODO: Implement
    }

    def getHeaderVariable() {
    // Gets the value of a Header Variable
        //TODO: Implement
    }

    def setHeaderVariable() {
     // Sets the value of a Header Variable
        //TODO: Implement
    }
    def dumpState() {
        //TODO: Implement
    }
}

