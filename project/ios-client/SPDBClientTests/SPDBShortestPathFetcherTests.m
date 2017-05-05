#import "SPDBRoute.h"
#import "SPDBShortestPathFetcher.h"
#import "SPDBShortestPathFetcherDelegate.h"
#import <XCTAsyncTestCase/XCTAsyncTestCase.h>

@interface SPDBShortestPathFetcherTests : XCTAsyncTestCase<SPDBShortestPathFetcherDelegate>

@property (nonatomic, strong) NSArray *shortestPath;

@end

@implementation SPDBShortestPathFetcherTests

- (void)setUp
{
    [super setUp];
}

- (void)tearDown
{
    [super tearDown];
}

- (void)testFetchShortestPathFromEntryMethod
{
    SPDBShortestPathFetcher *shortestPathFetcher = [SPDBShortestPathFetcher new];
    SPDBMapEntry *entryFrom = [SPDBMapEntry mapEntryWithLatitude:[NSNumber numberWithDouble:52.220067] andLongitude:[NSNumber numberWithDouble:21.012119]];
    SPDBMapEntry *entryTo = [SPDBMapEntry mapEntryWithLatitude:[NSNumber numberWithDouble:52.230014] andLongitude:[NSNumber numberWithDouble:21.011886]];
    
    [self prepare];
    
    [shortestPathFetcher fetchShortestPathFromEntry:entryFrom toEntry:entryTo isPublicTransport:NO withChangeTime:[NSNumber numberWithLong:0] delegate:self];
    
    [self waitForStatus:kXCTUnitWaitStatusSuccess timeout:10.0];
    
    NSUInteger mappedRoutePartsCount = [self.shortestPath count];
    SPDBRoute *firstPartOfRoute = [self shortestPathRouteForIndex:0];
    SPDBRoute *secondPartOfRoute = [self shortestPathRouteForIndex:1];
    SPDBRoute *thirdPartOfRoute = [self shortestPathRouteForIndex:2];
    SPDBRoute *fourthPartOfRoute = [self shortestPathRouteForIndex:3];
    
    XCTAssertNotNil(self.shortestPath);
    XCTAssertEqual(mappedRoutePartsCount, 4);
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

- (SPDBRoute *)shortestPathRouteForIndex:(NSUInteger)index
{
    if (index < [self.shortestPath count])
    {
        return self.shortestPath[index];
    }
    
    return nil;
}

- (void)updateProgress:(CGFloat)progress
{
}

- (void)didFetchShortestPath:(NSArray *)shortestPath
{
    self.shortestPath = shortestPath;
    [self notify:kXCTUnitWaitStatusSuccess];
}

- (void)didFailFetchingShortestPath:(NSError *)error
{
    [self notify:kXCTUnitWaitStatusFailure];
}

@end
