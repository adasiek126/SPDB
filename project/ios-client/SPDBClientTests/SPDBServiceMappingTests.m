#import <RestKit/RestKit.h>
#import <RestKit/Testing.h>
#import "SPDBMappingFactory.h"
#import <XCTest/XCTest.h>

@interface SPDBServiceMappingTests : XCTestCase

@end

@implementation SPDBServiceMappingTests

- (void)setUp
{
    [super setUp];

    NSBundle *testTargetBundle = [NSBundle bundleWithIdentifier:@"pl.edu.pw.elka.spdb.SPDBClientTests"];
    [RKTestFixture setFixtureBundle:testTargetBundle];
}

- (void)tearDown
{
    [super tearDown];
}

- (RKMappingTest *)mapEntryMappingTest
{
    SPDBMappingFactory *mappingFactory = [SPDBMappingFactory new];
    RKMapping *mapping = [mappingFactory createObjectMappingForMapEntry];
    id parsedJSON = [RKTestFixture parsedObjectWithContentsOfFixture:@"mapentry.json"];
    
    return [RKMappingTest testForMapping:mapping sourceObject:parsedJSON destinationObject:nil];
}

- (void)testMapEntryIdMapping
{
    RKPropertyMappingTestExpectation *expectation = [RKPropertyMappingTestExpectation
                                                        expectationWithSourceKeyPath:@"id"
                                                        destinationKeyPath:@"id"
                                                        value:[NSNumber numberWithLong:0]];
    
    XCTAssertTrue([[self mapEntryMappingTest] evaluateExpectation:expectation error:nil]);
}

- (void)testMapEntryLatitudeMapping
{
    RKPropertyMappingTestExpectation *expectation = [RKPropertyMappingTestExpectation
                                                        expectationWithSourceKeyPath:@"latitude"
                                                        destinationKeyPath:@"latitude"
                                                        value:[NSNumber numberWithDouble:52.231730]];
    
    XCTAssertTrue([[self mapEntryMappingTest] evaluateExpectation:expectation error:nil]);
}

- (void)testMapEntryLongitudeMapping
{
    RKPropertyMappingTestExpectation *expectation = [RKPropertyMappingTestExpectation
                                                        expectationWithSourceKeyPath:@"longitude"
                                                        destinationKeyPath:@"longitude"
                                                        value:[NSNumber numberWithDouble:21.005952]];
    
    XCTAssertTrue([[self mapEntryMappingTest] evaluateExpectation:expectation error:nil]);
}

- (RKMappingTest *)routeMappingTest
{
    SPDBMappingFactory *mappingFactory = [SPDBMappingFactory new];
    RKMapping *mapping = [mappingFactory createObjectMappingForRoute];
    id parsedJSON = [RKTestFixture parsedObjectWithContentsOfFixture:@"route.json"];
    
    return [RKMappingTest testForMapping:mapping sourceObject:parsedJSON destinationObject:nil];
}

- (void)testRouteIdMapping
{
    RKPropertyMappingTestExpectation *expectation = [RKPropertyMappingTestExpectation
                                                        expectationWithSourceKeyPath:@"id"
                                                        destinationKeyPath:@"id"
                                                        value:[NSNumber numberWithLong:2]];
    
    XCTAssertTrue([[self routeMappingTest] evaluateExpectation:expectation error:nil]);
}

- (void)testRouteFromMapping
{
    SPDBMappingFactory *mappingFactory = [SPDBMappingFactory new];
    RKMapping *mapEntryMapping = [mappingFactory createObjectMappingForMapEntry];
    RKPropertyMappingTestExpectation *expectation = [RKPropertyMappingTestExpectation
                                                        expectationWithSourceKeyPath:@"routeFrom"
                                                        destinationKeyPath:@"routeFrom"
                                                        mapping:mapEntryMapping];
    
    XCTAssertTrue([[self routeMappingTest] evaluateExpectation:expectation error:nil]);
}

- (void)testRouteToMapping
{
    SPDBMappingFactory *mappingFactory = [SPDBMappingFactory new];
    RKMapping *mapEntryMapping = [mappingFactory createObjectMappingForMapEntry];
    RKPropertyMappingTestExpectation *expectation = [RKPropertyMappingTestExpectation
                                                        expectationWithSourceKeyPath:@"routeTo"
                                                        destinationKeyPath:@"routeTo"
                                                        mapping:mapEntryMapping];
    
    XCTAssertTrue([[self routeMappingTest] evaluateExpectation:expectation error:nil]);
}

- (void)testRouteDurationMapping
{
    RKPropertyMappingTestExpectation *expectation = [RKPropertyMappingTestExpectation
                                                        expectationWithSourceKeyPath:@"duration"
                                                        destinationKeyPath:@"duration"
                                                        value:[NSNumber numberWithLong:300]];
    
    XCTAssertTrue([[self routeMappingTest] evaluateExpectation:expectation error:nil]);
}

@end
