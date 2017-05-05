#import "SPDBRoute.h"

@implementation SPDBRoute

- (id)init
{
    self = [super init];
    
    if (self != nil)
    {
        self.routeFrom = [SPDBMapEntry new];
        self.routeTo = [SPDBMapEntry new];
    }
    
    return self;
}

@end
