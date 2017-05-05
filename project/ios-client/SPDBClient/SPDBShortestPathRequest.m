#import "SPDBShortestPathRequest.h"

@implementation SPDBShortestPathRequest

+ (SPDBShortestPathRequest *)requestWithStartingNodeId:(NSNumber *)startingNodeId andFinishingNodeId:(NSNumber *)finishingNodeId
{
    return [SPDBShortestPathRequest requestWithStartingNodeId:startingNodeId andFinishingNodeId:finishingNodeId withPublicTransport:NO andChangeDuration:[NSNumber numberWithLong:0]];
}

+ (SPDBShortestPathRequest *)requestWithStartingNodeId:(NSNumber *)startingNodeId andFinishingNodeId:(NSNumber *)finishingNodeId withPublicTransport:(BOOL)publicTransport andChangeDuration:(NSNumber *)changeDuration
{
    SPDBShortestPathRequest *request = [SPDBShortestPathRequest new];
    request.startingNodeId = startingNodeId;
    request.finishingNodeId = finishingNodeId;
    request.publicTransport = publicTransport ? @"true" : @"false";
    request.changeDuration = changeDuration;
    
    return request;
}

@end
