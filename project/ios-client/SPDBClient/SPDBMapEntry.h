#import <Foundation/Foundation.h>

@interface SPDBMapEntry : NSObject

@property (nonatomic, strong) NSNumber *id;
@property (nonatomic, strong) NSNumber *latitude;
@property (nonatomic, strong) NSNumber *longitude;
@property (nonatomic, strong) NSString *publicTransportStop;

+ (id)mapEntryWithLatitude:(NSNumber *)latitude andLongitude:(NSNumber *)longitude;
+ (id)mapEntryWithLatitude:(NSNumber *)latitude andLongitude:(NSNumber *)longitude asPublicTransportStop:(BOOL)stop;

@end
