/**
 * Provides Java commons API.
 */
module org.panteleyev.commons {
    exports org.panteleyev.commons.xml;
    exports org.panteleyev.commons.password;
    exports org.panteleyev.commons.crypto;

    requires java.xml;
    requires java.logging;
}