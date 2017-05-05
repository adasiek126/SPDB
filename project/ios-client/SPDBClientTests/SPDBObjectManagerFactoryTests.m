#import <RestKit/RestKit.h>
#import "SPDBMapEntry.h"
#import "SPDBObjectManagerFactory.h"
#import "SPDBRoute.h"
#import "SPDBShortestPathRequest.h"
#import <XCTAsyncTestCase/XCTAsyncTestCase.h>

@interface SPDBObjectManagerFactoryTests : XCTAsyncTestCase

@property (nonatomic, strong) RKMappingResult *mappingResult;

@end

@implementation SPDBObjectManagerFactoryTests

- (void)setUp
{
    [super setUp];
}

- (void)tearDown
{
    [super tearDown];
}

- (void)testObjectManagerForNearestMapEntry
{
    RKObjectManager *objectManager = [self createObjectManager];
    SPDBMapEntry *mapEntry = [SPDBMapEntry mapEntryWithLatitude:[NSNumber numberWithDouble:52.220067] andLongitude:[NSNumber numberWithDouble:21.012119]];
    
    [self prepare];
    
    [objectManager getObject:mapEntry path:nil parameters:nil success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
        self.mappingResult = mappingResult;
        [self notify:kXCTUnitWaitStatusSuccess];
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        [self notify:kXCTUnitWaitStatusFailure];
    }];
    
    [self waitForStatus:kXCTUnitWaitStatusSuccess timeout:10.0];
    
    NSUInteger mappedObjectsCount = [[self.mappingResult array] count];
    mapEntry = [self mappingResultAtIndex:0];
    
    XCTAssertNotNil(self.mappingResult);
    XCTAssertEqual(mappedObjectsCount, 1);
    XCTAssertEqualObjects([NSNumber numberWithDouble:52.220067], mapEntry.latitude);
    XCTAssertEqualObjects([NSNumber numberWithDouble:21.012119], mapEntry.longitude);
}

- (id)mappingResultAtIndex:(NSUInteger)index
{
    if (index < [[self.mappingResult array] count])
    {
        return [self.mappingResult array][index];
    }
    
    return nil;
}

- (RKObjectManager *)createObjectManager
{
    SPDBObjectManagerFactory *objectManagerFactory = [SPDBObjectManagerFactory new];
    RKObjectManager *objectManager = [objectManagerFactory createObjectManager];
    
    return objectManager;
}

- (void)testObjectManagerForShortestPath
{
    RKObjectManager *objectManager = [self createObjectManager];
    SPDBShortestPathRequest *shortestPathRequest = [SPDBShortestPathRequest
                                                    requestWithStartingNodeId:[NSNumber numberWithLong:165408]
                                                    andFinishingNodeId:[NSNumber numberWithLong:165416]];
    
    [self prepare];
    
    [objectManager getObjectsAtPathForRouteNamed:@"ShortestPath" object:shortestPathRequest parameters:nil success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult)
    {
        self.mappingResult = mappingResult;
        [self notify:kXCTUnitWaitStatusSuccess];
    }
    failure:^(RKObjectRequestOperation *operation, NSError *error)
    {
        [self notify:kXCTUnitWaitStatusFailure];
    }];
    
    [self waitForStatus:kXCTUnitWaitStatusSuccess timeout:10.0];
    
    NSUInteger mappedObjectsCount = [[self.mappingResult array] count];
    SPDBRoute *firstPartOfRoute = [self mappingResultAtIndex:0];
    SPDBRoute *secondPartOfRoute = [self mappingResultAtIndex:1];
    SPDBRoute *thirdPartOfRoute = [self mappingResultAtIndex:2];
    SPDBRoute *fourthPartOfRoute = [self mappingResultAtIndex:3];
        
    XCTAssertNotNil(self.mappingResult);
    XCTAssertEqual(mappedObjectsCount, 4);
    XCTAssertNotNil(firstPartOfRoute.routeFrom);
    XCTAssertEqualObjects([NSNumber numberWithDouble:52.220067], firstPartOfRoute.routeFrom.latitude);
    XCTAssertEqualObjects([NSNumber numberWithDouble:21.012119], firstPartOfRoute.routeFrom.longitude);
    XCTAssertNotNil(firstPartOfRoute.routeTo);
    XCTAssertEqualObjects([NSNumber numberWithDouble:52.219893], firstPartOfRoute.routeTo.latitude);
    XCTAssertEqualObjects([NSNumber numberWithDouble:21.018152], firstPartOfRoute.routeTo.longitude);
    XCTAssertNotNil(secondPartOfRoute.routeFrom);
    XCTAssertEqualObjects([NSNumber numberWithDouble:52.219893], secondPartOfRoute.routeFrom.latitude);
    XCTAssertEqualObjects([NSNumber numberWithDouble:21.018152], secondPartOfRoute.routeFrom.longitude);
    XCTAssertNotNil(secondPartOfRoute.routeTo);
    XCTAssertEqualObjects([NSNumber numberWithDouble:52.223232], secondPartOfRoute.routeTo.latitude);
    XCTAssertEqualObjects([NSNumber numberWithDouble:21.015984], secondPartOfRoute.routeTo.longitude);
    XCTAssertNotNil(thirdPartOfRoute.routeFrom);
    XCTAssertEqualObjects([NSNumber numberWithDouble:52.223232], thirdPartOfRoute.routeFrom.latitude);
    XCTAssertEqualObjects([NSNumber numberWithDouble:21.015984], thirdPartOfRoute.routeFrom.longitude);
    XCTAssertNotNil(thirdPartOfRoute.routeTo);
    XCTAssertEqualObjects([NSNumber numberWithDouble:52.226229], thirdPartOfRoute.routeTo.latitude);
    XCTAssertEqualObjects([NSNumber numberWithDouble:21.014161], thirdPartOfRoute.routeTo.longitude);
    XCTAssertNotNil(fourthPartOfRoute.routeFrom);
    XCTAssertEqualObjects([NSNumber numberWithDouble:52.226229], fourthPartOfRoute.routeFrom.latitude);
    XCTAssertEqualObjects([NSNumber numberWithDouble:21.014161], fourthPartOfRoute.routeFrom.longitude);
    XCTAssertNotNil(fourthPartOfRoute.routeTo);
    XCTAssertEqualObjects([NSNumber numberWithDouble:52.230014], fourthPartOfRoute.routeTo.latitude);
    XCTAssertEqualObjects([NSNumber numberWithDouble:21.011886], fourthPartOfRoute.routeTo.longitude);
}

@end
