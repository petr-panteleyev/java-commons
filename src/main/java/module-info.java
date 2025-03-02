/**
 * Provides Java commons API.
 */
module org.panteleyev.commons {
    exports org.panteleyev.commons.xml;
    exports org.panteleyev.commons.password;
    exports org.panteleyev.commons.crypto;
    exports org.panteleyev.commons.functional;

    requires java.xml;
    requires java.logging;
}