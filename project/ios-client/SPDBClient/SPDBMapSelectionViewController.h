#import <MapKit/MapKit.h>
#import "SPDBMapSelectionDelegate.h"
#import <UIKit/UIKit.h>

@interface SPDBMapSelectionViewController : UIViewController

@property (nonatomic, strong) id<SPDBMapSelectionDelegate> delegate;

@property (nonatomic, strong) IBOutlet MKMapView *mapView;

@property (nonatomic, strong) NSValue *initialPoint;
@property (nonatomic) NSUInteger pointIndex;

@end
