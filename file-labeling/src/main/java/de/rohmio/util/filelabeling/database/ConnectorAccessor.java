package de.rohmio.util.filelabeling.database;

import de.rohmio.util.filelabeling.Constants;

// Singleton giving access to the database from everywhere in the code.
// Reduces the need to pass the SQLiteConnector itself up and down the chain.

public class ConnectorAccessor {
    /// Constructors
    private ConnectorAccessor(){
        mConnector = new SQLiteConnector(Constants.DataBaseName);
    }

    /// Methods
    public SQLiteConnector Connector(){
        return mConnector;
    }

    static public ConnectorAccessor Instance(){
        if(mAccessor==null){
            mAccessor = new ConnectorAccessor();
        }
        return mAccessor;
    }

    /// Members
    static private ConnectorAccessor mAccessor;
    static private SQLiteConnector mConnector;
}
