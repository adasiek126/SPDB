#import "SPDBMapEntry.h"

@implementation SPDBMapEntry

+ (id)mapEntryWithLatitude:(NSNumber *)latitude andLongitude:(NSNumber *)longitude
{
    return [SPDBMapEntry mapEntryWithLatitude:latitude andLongitude:longitude asPublicTransportStop:NO];
}

+ (id)mapEntryWithLatitude:(NSNumber *)latitude andLongitude:(NSNumber *)longitude asPublicTransportStop:(BOOL)stop
{
    SPDBMapEntry *mapEntry = [SPDBMapEntry new];
    
    mapEntry.latitude = latitude;
    mapEntry.longitude = longitude;
    mapEntry.publicTransportStop = stop ? @"true" : @"false";
    
    return mapEntry;
}

@end
