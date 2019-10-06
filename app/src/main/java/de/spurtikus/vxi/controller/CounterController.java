package de.spurtikus.vxi.controller;


import de.spurtikus.devices.hp.HP1333;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for counters.
 *
 * Tested with: * HP1333 Counter. See class {HP1333}.
 *
 * @author dennis
 *
 */
@RestController
@RequestMapping(Constants.SERVICE_ROOT + Constants.URL_COUNTER)
public class CounterController extends AbstractController<HP1333> {
}
