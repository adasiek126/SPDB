#import <Foundation/Foundation.h>
#import <RestKit/RestKit.h>

@interface SPDBObjectManagerFactory : NSObject

- (RKObjectManager *)createObjectManager;

@end
