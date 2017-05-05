#import <Foundation/Foundation.h>
#import "SPDBMapEntry.h"

@interface SPDBRoute : NSObject

@property (nonatomic, strong) NSNumber *id;
@property (nonatomic, strong) SPDBMapEntry *routeFrom;
@property (nonatomic, strong) SPDBMapEntry *routeTo;
@property (nonatomic, strong) NSNumber *duration;
@property (nonatomic, strong) NSNumber *line;

@end
