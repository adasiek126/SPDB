#import "SPDBConfigurationProvider.h"
#import "SPDBMapEntry.h"
#import "SPDBMappingFactory.h"
#import "SPDBObjectManagerFactory.h"

@implementation SPDBObjectManagerFactory

- (RKObjectManager *)createObjectManager
{
    SPDBMappingFactory *mappingFactory = [SPDBMappingFactory new];
    
    RKObjectManager *manager = [RKObjectManager managerWithBaseURL:[NSURL URLWithString:[SPDBConfigurationProvider getServiceAddress]]];
    
    [manager.router.routeSet addRoute:[RKRoute routeWithClass:[SPDBMapEntry class] pathPattern:@"entry/nearest/:latitude/:longitude/publicTransportStop/:publicTransportStop" method:RKRequestMethodGET]];
    [manager.router.routeSet addRoute:[RKRoute routeWithName:@"ShortestPath" pathPattern:@"entry/shortestPath/:startingNodeId/:finishingNodeId/publicTransport/:publicTransport/changeDuration/:changeDuration" method:RKRequestMethodGET]];
    
    [manager addResponseDescriptor:[RKResponseDescriptor responseDescriptorWithMapping:[mappingFactory createObjectMappingForMapEntry] method:RKRequestMethodGET pathPattern:@"entry/nearest/:latitude/:longitude/publicTransportStop/:publicTransportStop" keyPath:nil statusCodes:[NSIndexSet indexSetWithIndex:200]]];
    [manager addResponseDescriptor:[RKResponseDescriptor responseDescriptorWithMapping:[mappingFactory createObjectMappingForRoute] method:RKRequestMethodGET pathPattern:@"entry/shortestPath/:startingNodeId/:finishingNodeId/publicTransport/:publicTransport/changeDuration/:changeDuration" keyPath:nil statusCodes:[NSIndexSet indexSetWithIndex:200]]];
    
    return manager;
}

@end
