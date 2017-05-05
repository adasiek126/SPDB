#import "SPDBShortestPathFetcher.h"
#import "SPDBShortestPathRequest.h"

@implementation SPDBShortestPathFetcher

- (id)init
{
    self = [super init];
    
    if (self != nil)
    {
        self.objectManagerFactory = [SPDBObjectManagerFactory new];
    }
    
    return self;
}

- (void)fetchShortestPathFromEntry:(SPDBMapEntry *)fromEntry toEntry:(SPDBMapEntry *)toEntry isPublicTransport:(BOOL)publicTransport withChangeTime:(NSNumber *)changeTime delegate:(id<SPDBShortestPathFetcherDelegate>)delegate
{
    self.delegate = delegate;
        
    [self fetchClosestPoint:fromEntry success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult)
    {
        self.entryFrom = [mappingResult array][0];
        [self.delegate updateProgress:0.333];
        [self fetchClosestPoint:toEntry success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult)
        {
            self.entryTo = [mappingResult array][0];
            [self.delegate updateProgress:0.666];
            [self fetchShortestPath:self.entryFrom to:self.entryTo isPublicTransport:publicTransport withChangeTime:changeTime success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult)
            {
                [self.delegate updateProgress:1.0];
                [self.delegate didFetchShortestPath:[mappingResult array]];
            }
            failure:^(RKObjectRequestOperation *operation, NSError *error)
            {
                [self.delegate didFailFetchingShortestPath:error];
            }];
        }
        failure:^(RKObjectRequestOperation *operation, NSError *error)
        {
            [self.delegate didFailFetchingShortestPath:error];
        }];
    }
    failure:^(RKObjectRequestOperation *operation, NSError *error)
    {
        [self.delegate didFailFetchingShortestPath:error];
    }];
}

- (void)fetchClosestPoint:(SPDBMapEntry *)mapEntry success:(void (^)(RKObjectRequestOperation *operation, RKMappingResult *mappingResult))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure
{
    RKObjectManager *objectManager = [self.objectManagerFactory createObjectManager];
    [objectManager getObject:mapEntry path:nil parameters:nil success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult)
    {
         success(operation, mappingResult);
    }
    failure:^(RKObjectRequestOperation *operation, NSError *error)
    {
         failure(operation, error);
    }];
}

- (void)fetchShortestPath:(SPDBMapEntry *)fromEntry to:(SPDBMapEntry *)toEntry isPublicTransport:(BOOL)publicTransport withChangeTime:(NSNumber *)changeTime success:(void (^)(RKObjectRequestOperation *operation, RKMappingResult *mappingResult))success failure:(void (^)(RKObjectRequestOperation *operation, NSError *error))failure
{
    RKObjectManager *objectManager = [self.objectManagerFactory createObjectManager];
    SPDBShortestPathRequest *shortestPathRequest = [SPDBShortestPathRequest requestWithStartingNodeId:fromEntry.id andFinishingNodeId:toEntry.id withPublicTransport:publicTransport andChangeDuration:changeTime];
    [objectManager getObjectsAtPathForRouteNamed:@"ShortestPath" object:shortestPathRequest parameters:nil success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult)
    {
        success(operation, mappingResult);
    }
    failure:^(RKObjectRequestOperation *operation, NSError *error) {
        failure(operation, error);
    }];
}

@end
