
class SymbolTable {

    enum KindVar {
        NONE, STATIC, FIELD, ARG, LOCAL
    }

    private class Symbol {
        private String mName;
        private String mType;
        private KindVar mKind;
        private int mIndex;

        Symbol(String name, String type, KindVar kind, int index) {
            mName = name;
            mType = type;
            mKind = kind;
            mIndex = index;
        }

         String getName() {
            return mName;
        }

         String getType() {
            return mType;
        }

         KindVar getKind() {
            return mKind;
        }

         int getIndex() {
            return mIndex;
        }
    }

    private HashMap<String, Symbol> mSymbolsTable = new HashMap<>();
    private int mCountStaticVars = 0;
    private int mCountFieldVars = 0;
    private int mCountArgVars = 0;
    private int mCountLocalVars = 0;

     void startSubroutine() {
        mSymbolsTable.clear();

        mCountStaticVars = 0;
        mCountFieldVars = 0;
        mCountArgVars = 0;
        mCountLocalVars = 0;
    }

     void define(String name, String type, KindVar kindVar) {
        switch (kindVar) {
            case KindVar.STATIC: {
                mSymbolsTable.put(name, new Symbol(name, type, kindVar, mCountStaticVars++));
            } ;break;

            case KindVar.FIELD: {
                mSymbolsTable.put(name, new Symbol(name, type, kindVar, mCountFieldVars++));
            }; break;

            case KindVar.ARG: {
                mSymbolsTable.put(name, new Symbol(name, type, kindVar, mCountArgVars++));
            } ;break;

            default: {
                mSymbolsTable.put(name, new Symbol(name, type, kindVar, mCountLocalVars++));
            } ;break;
        }
    }

     int varCount(KindVar kindVar) {
        switch (kindVar) {
            case KindVar.STATIC: {
                return mCountStaticVars;
            }

            case KindVar.FIELD: {
                return mCountFieldVars;
            }

            case KindVar.ARG: {
                return mCountArgVars;
            }

            default: {
                return mCountLocalVars;
            }
        }
    }

     KindVar kindOf(String name) {
        if (mSymbolsTable.containsKey(name)) {
            return mSymbolsTable.get(name).getKind();
        }

        return KindVar.NONE;
    }

     String typeOf(String name) {
        return mSymbolsTable.get(name).getType();
    }

     int indexOf(String name) {
         var m = mSymbolsTable;

        return mSymbolsTable.get(name).getIndex();
    }

}
