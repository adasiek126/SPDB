#import <Foundation/Foundation.h>
#import <RestKit/RestKit.h>

@interface SPDBMappingFactory : NSObject

- (RKObjectMapping *)createObjectMappingForMapEntry;
- (RKObjectMapping *)createObjectMappingForRoute;

@end
