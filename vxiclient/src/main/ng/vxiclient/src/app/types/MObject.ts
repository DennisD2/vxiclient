/**
 * Root of all managed 'measurement' objects.
 */
export interface MObject {
    // getType();
    getName() ;
    start();
    stop();
}
